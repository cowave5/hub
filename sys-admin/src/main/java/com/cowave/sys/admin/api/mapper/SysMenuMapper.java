/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cowave.sys.admin.api.entity.RoleAuthed;
import com.cowave.sys.model.admin.SysMenu;
import com.cowave.sys.model.admin.SysRole;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysMenuMapper{

	/**
	 * 所有菜单
	 */
	List<SysMenu> list(@Param("visible") Integer visible,
					   @Param("menuName") String menuName, @Param("menuStatus") Integer menuStatus);

	/**
	 * 用户菜单
	 */
	List<SysMenu> userMenus(@Param("userId") Long userId, @Param("visible") Integer visible,
							@Param("menuName") String menuName, @Param("menuStatus") Integer menuStatus);

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
	void insert(SysMenu sysMenu);

	/**
	 * 更新
	 */
	void update(SysMenu sysMenu);

	/**
	 * 删除角色菜单
	 */
	void deleteMenus(Long menuId);

	/**
	 * 删除菜单
	 */
	void deletes(Long menuId);

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

	/**
	 * 修改只读
	 */
	void changeReadonly(SysMenu sysMenu);
}
