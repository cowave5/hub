/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cowave.sys.admin.api.entity.UserAuthed;
import com.cowave.sys.admin.api.mapper.SysRoleMapper;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public Page<SysRole> list(SysRole sysRole) {
        return sysRoleMapper.list(Access.page(), sysRole);
    }

    @Override
    public SysRole info(Long roleId) {
        return sysRoleMapper.info(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysRole sysRole) {
    	int codeCount = sysRoleMapper.countRoleCode(sysRole.getRoleCode(), null);
    	Asserts.equals(0, codeCount, "{role.conflict.code}", sysRole.getRoleCode());
    	// 新增角色
    	sysRoleMapper.insert(sysRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole edit(SysRole sysRole) {
    	Asserts.notNull(sysRole.getRoleId(), "{role.notnull.id}");
    	int codeCount = sysRoleMapper.countRoleCode(sysRole.getRoleCode(), sysRole.getRoleId());
    	Asserts.equals(0, codeCount, "{role.conflict.code}", sysRole.getRoleCode());

    	SysRole preRole = sysRoleMapper.info(sysRole.getRoleId());
    	Asserts.notNull(preRole, "{role.notexist}", sysRole.getRoleId());
    	Asserts.notEquals(1, preRole.getReadOnly(), "{role.forbid.edit.readonly}", preRole.getRoleName());
        // 更新角色
    	sysRoleMapper.update(sysRole);
    	return preRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysRole> delete(Long[] roleId) {
        Asserts.equals(0, sysRoleMapper.countReadOnlyByIdArray(roleId), "{role.forbid.delete.readonly}");
        List<SysRole> list = sysRoleMapper.queryByIdArray(roleId);
        // 删除角色
    	sysRoleMapper.deleteByIdArray(roleId);
        // 角色菜单
        sysRoleMapper.deleteRoleMenusByIdArray(roleId);
        // 角色用户
        sysRoleMapper.deleteRoleUsersByIdArray(roleId);
    	return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeMenus(SysRole sysRole) {
        Asserts.notNull(sysRole.getRoleId(), "{role.notnull.id}");
        SysRole preRole = sysRoleMapper.info(sysRole.getRoleId());
        Asserts.notNull(preRole, "{role.notexist}", sysRole.getRoleId());
        Asserts.notEquals(1, preRole.getReadOnly(), "{role.forbid.edit.readonly}", preRole.getRoleName());

        sysRoleMapper.deleteRoleMenus(sysRole.getRoleId());
        sysRoleMapper.insertRoleMenus(sysRole.getRoleId(), sysRole.getMenuIds());
    }

    @Override
    public List<SysUser> userAuthed(UserAuthed userAuthed) {
        return sysRoleMapper.userAuthed(userAuthed);
    }

    @Override
    public List<SysUser> userUnAuthed(UserAuthed userAuthed) {
        return sysRoleMapper.userUnAuthed(userAuthed);
    }

    @Override
    public void grant(UserAuthed userAuthed) {
        Asserts.notNull(userAuthed.getUserIds(), "{role.notnull.userIds}");
        sysRoleMapper.grant(userAuthed);
    }

    @Override
    public void cancel(UserAuthed userAuthed) {
        Asserts.notNull(userAuthed.getUserIds(), "{role.notnull.userIds}");
        sysRoleMapper.cancel(userAuthed);
    }

    @Override
    public void changeReadonly(SysRole sysRole) {
        sysRoleMapper.changeReadonly(sysRole);
    }
}
