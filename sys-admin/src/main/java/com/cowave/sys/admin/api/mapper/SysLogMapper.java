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
import com.cowave.sys.model.admin.SysLog;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysLogMapper {

	/**
	 * 列表
	 */
	Page<SysLog> list(Page<SysLog> page, @Param("log") SysLog sysLog);

	/**
	 * 新增
	 */
	void insert(SysLog sysLog);

	/**
	 * 详情
	 */
	SysLog info(long id);

	/**
	 * 删除
	 */
	void delete(Long[] array);

	/**
	 * 清空
	 */
	void clean();

	/**
	 * 查询角色名称
	 */
	List<String> queryNameByRoleIds(List<Long> list);

	/**
	 * 查询用户名称
	 */
	List<String> queryNameByUserIds(List<Long> list);

	/**
	 * 查询部门名称
	 */
	List<String> queryNameByDeptIds(List<Long> list);

	/**
	 * 查询岗位名称
	 */
	String queryNameByPostId(Long postId);

	/**
	 * 查询部门岗位名称
	 */
	String queryNameOfDeptPost(@Param("deptId") Long deptId, @Param("postId") Long postId);
}
