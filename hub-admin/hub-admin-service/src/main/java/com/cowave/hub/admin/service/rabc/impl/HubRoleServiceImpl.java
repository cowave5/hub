/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.rabc.HubRole;
import com.cowave.hub.admin.domain.rabc.HubRoleMenu;
import com.cowave.hub.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.hub.admin.domain.rabc.request.*;
import com.cowave.hub.admin.infra.rabc.dao.HubRoleDao;
import com.cowave.hub.admin.infra.rabc.dao.HubRoleMenuDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserRoleDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubRoleDtoMapper;
import com.cowave.hub.admin.service.rabc.HubRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubRoleServiceImpl implements HubRoleService {
    private final HubRoleDao hubRoleDao;
    private final HubRoleMenuDao hubRoleMenuDao;
    private final HubUserRoleDao hubUserRoleDao;
    private final HubRoleDtoMapper hubRoleDtoMapper;

    @Override
    public Page<HubRole> list(String tenantId, RoleQuery query) {
        return hubRoleDao.queryPage(tenantId, query);
    }

    @Override
    public RoleInfoDto info(String tenantId, Integer roleId) {
        return hubRoleDtoMapper.info(tenantId, roleId);
    }

    @Override
    public void add(String tenantId, HubRole hubRole) {
    	long codeCount = hubRoleDao.countRoleCode(tenantId, hubRole.getRoleCode(), null);
    	HttpAsserts.isTrue(codeCount == 0,
                BAD_REQUEST, "{admin.role.code.conflict}", hubRole.getRoleCode());
    	// 新增角色
        hubRoleDao.save(hubRole);
    }

    @Override
    public void delete(String tenantId, List<Integer> roleIds) {
        // 操作日志
        List<HubRole> list = hubRoleDao.listByIds(tenantId, roleIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubRole::getRoleId);
        if (!deleteList.isEmpty()) {
            // 删除角色
            hubRoleDao.removeByIds(deleteList);
            // 角色菜单
            hubRoleMenuDao.deleteByRoleIds(deleteList);
            // 角色用户
            hubUserRoleDao.deleteByRoleIds(deleteList);
        }
    }

    @Override
    public void edit(String tenantId, HubRole hubRole) {
    	HttpAsserts.notNull(hubRole.getRoleId(), BAD_REQUEST, "{admin.role.id.null}");

    	long codeCount = hubRoleDao.countRoleCode(tenantId, hubRole.getRoleCode(), hubRole.getRoleId());
    	HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", hubRole.getRoleCode());

        // 校验&操作日志
    	HubRole preRole = hubRoleDao.getById(tenantId, hubRole.getRoleId());
    	HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", hubRole.getRoleId());
        OperationContext.prepareContent(preRole);

        // 更新角色
        hubRoleDao.updateRole(hubRole);
    }

    @Override
    public void updateMenus(String tenantId, RoleMenuUpdate roleUpdate) {
        HubRole preRole = hubRoleDao.getById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());

        hubRoleMenuDao.deleteByRoleId(roleUpdate.getRoleId());
        hubRoleMenuDao.saveBatch(Collections.copyToList(roleUpdate.getMenuScopes(),
                ms -> new HubRoleMenu(roleUpdate.getRoleId(), ms.getMenuId(), ms.getScopeId())));
    }

    @Override
    public void grantUser(String tenantId, RoleUserUpdate roleUpdate) {
        hubRoleDtoMapper.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void cancelUser(String tenantId, RoleUserUpdate roleUpdate) {
        HubRole preRole = hubRoleDao.getById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());
        // 删除角色用户
        hubUserRoleDao.deleteRoleUsers(roleUpdate.getRoleId(), roleUpdate.getUserIds());
    }

    @Override
    public Page<RoleUserDto> getAuthedUser(String tenantId, RoleUserQuery query) {
        return hubRoleDtoMapper.getAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public Page<RoleUserDto> getUnAuthedUser(String tenantId, RoleUserQuery query) {
        return hubRoleDtoMapper.getUnAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public List<String> getNames(String tenantId, List<Integer> roleIds) {
        return hubRoleDao.queryNameByIds(tenantId, roleIds);
    }
}
