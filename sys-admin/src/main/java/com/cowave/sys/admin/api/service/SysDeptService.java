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
import com.cowave.sys.model.admin.SysDept;
import com.cowave.sys.model.admin.SysDeptPost;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysDeptService{

	/**
	 * 列表
	 */
	Page<SysDept> list(SysDept sysDept);

	/**
	 * 详情
	 */
	SysDept info(Long deptId);

	/**
	 * 新增
	 */
	void add(SysDept sysDept) throws Exception;

	/**
	 * 修改
	 */
	SysDept edit(SysDept sysDept) throws Exception;

	/**
	 * 删除
	 */
	List<SysDept> delete(Long[] deptId) throws Exception;

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDept sysDept);

	/**
	 * 获取部门岗位
	 */
	List<SysDeptPost> getPostsById(Long deptId);

	/**
	 * 设置部门岗位
	 */
	void setPosts(List<SysDeptPost> list);

	/**
	 * 部门人员，id获取
	 */
	List<SysUserDept> getUsersById(Long deptId);

	/**
	 * 设置部门人员
	 */
	void setUsers(List<SysUserDept> list);

	/**
	 * 部门人员，code获取
	 */
	List<SysUser> getUsersByCode(String deptCode);
}
