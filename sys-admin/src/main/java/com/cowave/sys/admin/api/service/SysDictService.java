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
import com.cowave.sys.admin.api.entity.SelectOption;
import com.cowave.sys.model.admin.SysDict;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysDictService{

	/**
	 * 类型选项
	 */
	List<SelectOption> dictOptions();

	/**
	 * Group子类型选项
	 */
	List<SelectOption> groupOptions(String groupCode);

	/**
	 * 列表
	 */
	Page<SysDict> list(String groupCode, String typeCode);

	/**
	 * 详情
	 */
	SysDict info(Long dictId);

	/**
	 * 新增
	 */
	void add(SysDict sysDict);

	/**
	 * 修改
	 */
	void edit(SysDict sysDict);

	/**
	 * 删除
	 */
	void delete(Long[] dictId);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDict sysDict);
}
