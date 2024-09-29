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
    public Page<SysRole> list(RoleQuery query) {
        return sysRoleDao.queryPage(query);
    }

    @Override
    public RoleInfoDto info(Integer roleId) {
        return sysRoleDtoMapper.info(roleId);
    }

    @Override
    public void add(SysRole sysRole) {
    	long codeCount = sysRoleDao.countRoleCode(sysRole.getRoleCode(), null);
    	HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());
    	// 新增角色
        sysRoleDao.save(sysRole);
    }

    @Override
    public void delete(List<Integer> roleIds) {
        // 操作日志
        List<SysRole> list = sysRoleDao.listByIds(roleIds);
        OperationContext.prepareContent(list);

        // 删除角色
        sysRoleDao.removeByIds(roleIds);
        // 角色菜单
        sysRoleMenuDao.deleteByRoleIds(roleIds);
        // 角色用户
        sysUserRoleDao.deleteByRoleIds(roleIds);
    }

    @Override
    public void edit(SysRole sysRole) {
    	HttpAsserts.notNull(sysRole.getRoleId(), BAD_REQUEST, "{admin.role.id.notnull}");

    	long codeCount = sysRoleDao.countRoleCode(sysRole.getRoleCode(), sysRole.getRoleId());
    	HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());

        // 校验&操作日志
    	RoleInfoDto preRole = sysRoleDtoMapper.info(sysRole.getRoleId());
    	HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", sysRole.getRoleId());
        OperationContext.prepareContent(preRole);

        // 更新角色
        sysRoleDao.updateRole(sysRole);
    }

    @Override
    public void updateMenus(RoleMenuUpdate roleUpdate) {
        RoleInfoDto preRole = sysRoleDtoMapper.info(roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());

        sysRoleMenuDao.deleteByRoleId(roleUpdate.getRoleId());
        sysRoleMenuDao.saveBatch(Collections.copyToList(
                roleUpdate.getMenuIds(), mid -> new SysRoleMenu(roleUpdate.getRoleId(), mid)));
    }

    @Override
    public void grantUser(RoleUserUpdate roleUpdate) {
        sysRoleDtoMapper.addRoleUser(roleUpdate);
    }

    @Override
    public void cancelUser(RoleUserUpdate roleUpdate) {
        sysUserRoleDao.deleteUserRoles(roleUpdate.getRoleId(), roleUpdate.getUserIds());
    }

    @Override
    public Page<RoleUserDto> getAuthedUser(RoleUserQuery query) {
        return sysRoleDtoMapper.getAuthedUser(Access.page(), query);
    }

    @Override
    public Page<RoleUserDto> getUnAuthedUser(RoleUserQuery query) {
        return sysRoleDtoMapper.getUnAuthedUser(Access.page(), query);
    }

    @Override
    public List<String> getNames(List<Integer> roleIds) {
        return sysRoleDao.queryNameByIds(roleIds);
    }
}
