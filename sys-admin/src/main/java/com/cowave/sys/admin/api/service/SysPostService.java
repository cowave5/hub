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
import com.cowave.sys.model.admin.SysPost;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysPostService {

	/**
	 * 列表
	 */
	Page<SysPost> list(SysPost sysPost);

	/**
	 * 详情
	 */
	SysPost info(Long postId);

	/**
	 * 新增
	 */
	void add(SysPost sysPost);

	/**
	 * 修改
	 */
	SysPost edit(SysPost sysPost);

	/**
	 * 删除
	 */
	List<SysPost> delete(Long[] postId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysPost sysPost);

	/**
	 * 岗位人员，code获取
	 */
	List<SysUser> getUsersByCode(String postCode);
}
