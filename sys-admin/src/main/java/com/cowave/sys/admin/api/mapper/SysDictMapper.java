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
import com.cowave.sys.admin.api.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;

import com.cowave.sys.model.admin.SysDict;
import org.apache.ibatis.annotations.Param;

/**
 * 字典
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysDictMapper{

	/**
	 * 字典类型选项
	 */
	List<SelectOption> dictOptions();

	/**
	 * Group子类型选项
	 */
	List<SelectOption> groupOptions(String groupCode);

	/**
	 * 列表
	 */
	Page<SysDict> list(Page<SysDict> page, @Param("groupCode") String groupCode, @Param("typeCode") String typeCode);

	/**
	 * 通过id查询字典
	 */
	SysDict queryById(long id);

	/**
	 * 新增
	 */
	void insert(SysDict sysDict);

	/**
	 * 修改
	 */
	void update(SysDict sysDict);

	/**
	 * 删除
	 */
	void delete(Long dictId);

	/**
	 * 删除类型
	 */
	void deleteType(String typeCode);

	/**
	 * 删除分组
	 */
	void deleteGroup(String groupCode);

	/**
	 * 修改只读
	 */
	void changeReadonly(SysDict sysDict);

	/**
	 * 字典查询
	 */
	List<SysDict> queryByIdArray(Long[] array);

	/**
	 * 子字典查询
	 */
	List<SysDict> queryByParentCode(String parentCode);

	/**
	 * 更新字典类型码
	 */
	void updateChildren(@Param("parentCode") String parentCode, @Param("preCode") String preCode);

	/**
	 * 计数 只读字典
	 */
	int countReadOnlyByIdArray(Long[] array);
}
