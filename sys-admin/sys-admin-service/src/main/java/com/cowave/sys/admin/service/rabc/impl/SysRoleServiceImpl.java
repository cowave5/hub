/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysRole;
import com.cowave.sys.admin.domain.rabc.SysRoleMenu;
import com.cowave.sys.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.infra.rabc.dao.SysRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.SysRoleMenuDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysRoleDtoMapper;
import com.cowave.sys.admin.service.rabc.SysRoleService;
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
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleDao sysRoleDao;
    private final SysRoleMenuDao sysRoleMenuDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysRoleDtoMapper sysRoleDtoMapper;

    @Override
    public Page<SysRole> list(String tenantId, RoleQuery query) {
        return sysRoleDao.queryPage(tenantId, query);
    }

    @Override
    public RoleInfoDto info(String tenantId, Integer roleId) {
        return sysRoleDtoMapper.info(tenantId, roleId);
    }

    @Override
    public void add(String tenantId, SysRole sysRole) {
    	long codeCount = sysRoleDao.countRoleCode(tenantId, sysRole.getRoleCode(), null);
    	HttpAsserts.isTrue(codeCount == 0,
                BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());
    	// 新增角色
        sysRoleDao.save(sysRole);
    }

    @Override
    public void delete(String tenantId, List<Integer> roleIds) {
        // 操作日志
        List<SysRole> list = sysRoleDao.listByIds(tenantId, roleIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, SysRole::getRoleId);
        if (!deleteList.isEmpty()) {
            // 删除角色
            sysRoleDao.removeByIds(deleteList);
            // 角色菜单
            sysRoleMenuDao.deleteByRoleIds(deleteList);
            // 角色用户
            sysUserRoleDao.deleteByRoleIds(deleteList);
        }
    }

    @Override
    public void edit(String tenantId, SysRole sysRole) {
    	HttpAsserts.notNull(sysRole.getRoleId(), BAD_REQUEST, "{admin.role.id.notnull}");

    	long codeCount = sysRoleDao.countRoleCode(tenantId, sysRole.getRoleCode(), sysRole.getRoleId());
    	HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());

        // 校验&操作日志
    	SysRole preRole = sysRoleDao.getById(tenantId, sysRole.getRoleId());
    	HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", sysRole.getRoleId());
        OperationContext.prepareContent(preRole);

        // 更新角色
        sysRoleDao.updateRole(sysRole);
    }

    @Override
    public void updateMenus(String tenantId, RoleMenuUpdate roleUpdate) {
        SysRole preRole = sysRoleDao.getById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());

        sysRoleMenuDao.deleteByRoleId(roleUpdate.getRoleId());
        sysRoleMenuDao.saveBatch(Collections.copyToList(
                roleUpdate.getMenuIds(), mid -> new SysRoleMenu(roleUpdate.getRoleId(), mid)));
    }

    @Override
    public void grantUser(String tenantId, RoleUserUpdate roleUpdate) {
        sysRoleDtoMapper.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void cancelUser(String tenantId, RoleUserUpdate roleUpdate) {
        SysRole preRole = sysRoleDao.getById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());
        // 删除角色用户
        sysUserRoleDao.deleteRoleUsers(roleUpdate.getRoleId(), roleUpdate.getUserIds());
    }

    @Override
    public Page<RoleUserDto> getAuthedUser(String tenantId, RoleUserQuery query) {
        return sysRoleDtoMapper.getAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public Page<RoleUserDto> getUnAuthedUser(String tenantId, RoleUserQuery query) {
        return sysRoleDtoMapper.getUnAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public List<String> getNames(String tenantId, List<Integer> roleIds) {
        return sysRoleDao.queryNameByIds(tenantId, roleIds);
    }
}
