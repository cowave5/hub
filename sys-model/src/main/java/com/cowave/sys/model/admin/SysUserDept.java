/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户部门
 *
 * @author shanhuiming
 *
 */
@Data
public class SysUserDept {

	/**
	 * 用户id
	 */
	@NotNull(message = "{user.notnull.id}")
	private Long userId;

	/**
	 * 用户编码
	 */
	private String userCode;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 部门id
	 */
	@NotNull(message = "{dept.notnull.id}")
	private Long deptId;

	/**
	 * 部门编码
	 */
	private String deptCode;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 岗位id
	 */
	@NotNull(message = "{post.notnull.id}")
	private Long postId;

	/**
	 * 岗位编码
	 */
	private String postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 是否用户默认单位
	 */
	private Integer isDefault = 0;

	/**
	 * 是否单位负责人
	 */
	private Integer isLeader = 0;

	public static SysUserDept parseDeptPost(String deptPostId, Long userId){
		String[] arr = deptPostId.split("-");
		SysUserDept userDept = new SysUserDept();
		userDept.setUserId(userId);
		userDept.setDeptId(Long.parseLong(arr[0]));
		userDept.setPostId(Long.parseLong(arr[1]));
		return userDept;
	}
}
