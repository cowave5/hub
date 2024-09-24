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

import com.cowave.sys.admin.api.entity.RoleAuthed;
import com.cowave.sys.model.admin.SysMenu;
import com.cowave.sys.model.admin.SysRole;

import cn.hutool.core.lang.tree.Tree;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysMenuService{

	/**
	 * 树
	 */
	List<Tree<Long>> tree();

	/**
	 * 列表
	 */
	List<SysMenu> list(String menuName, Integer menuStatus, Integer visible, boolean filterRole);

	/**
	 * OAuth菜单
	 */
	List<SysMenu> oauthMenus(Long userId);

	/**
	 * 详情
	 */
	SysMenu info(Long menuId);

	/**
	 * 新增
	 */
	void add(SysMenu sysMenu);

	/**
	 * 修改
	 */
	void edit(SysMenu sysMenu);

	/**
	 * 删除
	 */
	void delete(Long menuId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysMenu sysMenu);

	/**
     * 已授权角色
     */
    List<SysRole> roleAuthed(RoleAuthed roleAuthed);

    /**
     * 未授权角色
     */
    List<SysRole> roleUnAuthed(RoleAuthed roleAuthed);

    /**
     * 授权角色
     */
    void grant(RoleAuthed roleAuthed);

    /**
     * 取消授权
     */
    void cancel(RoleAuthed roleAuthed);
}
