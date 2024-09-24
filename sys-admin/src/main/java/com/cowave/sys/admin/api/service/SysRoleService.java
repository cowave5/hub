/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.UserAuthed;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysRoleService {

    /**
     * 列表
     */
    Page<SysRole> list(SysRole role);

    /**
     * 详情
     */
    SysRole info(Long roleId);

    /**
     * 新增
     */
    void add(SysRole sysRole);

    /**
     * 修改
     */
    SysRole edit(SysRole sysRole);

    /**
     * 删除
     */
    List<SysRole> delete(Long[] roleId);

    /**
     * 修改角色菜单
     */
    void changeMenus(SysRole sysRole);

    /**
     * 已授权用户
     */
    List<SysUser> userAuthed(UserAuthed userAuthed);

    /**
     * 未授权用户
     */
    List<SysUser> userUnAuthed(UserAuthed userAuthed);

    /**
     * 授权用户
     */
    void grant(UserAuthed userAuthed);

    /**
     * 取消授权
     */
    void cancel(UserAuthed userAuthed);

    /**
     * 修改只读
     */
    void changeReadonly(SysRole sysRole);
}
