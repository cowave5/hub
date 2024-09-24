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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.UserAuthed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;

/**
 * 角色
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysRoleMapper {

	/**
	 * 列表
	 */
	Page<SysRole> list(Page<SysRole> page, @Param("role") SysRole sysRole);

	/**
	 * 新增
	 */
	void insert(SysRole sysRole);

	/**
	 * 详情
	 */
	SysRole info(Long roleId);

	/**
	 * 角色名称计数
	 */
	int countRoleName(@Param("roleName") String roleName, @Param("roleId") Long roleId);

	/**
	 * 角色编码计数
	 */
	int countRoleCode(@Param("roleCode") String roleCode, @Param("roleId") Long roleId);

	/**
	 * 更新
	 */
	void update(SysRole sysRole);

	/**
	 * 删除
	 */
	void delete(Long roleId);

	/**
	 * 删除角色用户
	 */
	void deleteUsers(Long roleId);

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
	 * 只读计数
	 */
	int countReadOnlyByIdArray(Long[] array);

	/**
	 * id列表查询
	 */
	List<SysRole> queryByIdArray(Long[] array);

	/**
	 * 删除
	 */
	int deleteByIdArray(Long[] array);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysRole sysRole);

	/**
	 * 删除角色菜单
	 */
	int deleteRoleMenusByIdArray(Long[] array);

	/**
	 * 删除角色用户
	 */
	int deleteRoleUsersByIdArray(Long[] array);

	/**
	 * 删除角色菜单
	 */
	void deleteRoleMenus(Long roleId);

	/**
	 * 插入角色菜单
	 */
	void insertRoleMenus(@Param("roleId") long roleId, @Param("list") List<Long> menuIds);

	/**
	 * 获取角色code
	 */
	String queryCodeById(Long id);
}
