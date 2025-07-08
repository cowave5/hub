/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base;

import com.cowave.sys.admin.domain.base.SysDict;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.domain.base.request.DictQuery;
import com.cowave.sys.admin.domain.base.vo.SelectOption;

import java.util.Collection;
import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDictService{

	/**
	 * 列表
	 */
	List<DictInfoDto> queryList(DictQuery query);

	/**
	 * 详情
	 */
	DictInfoDto info(Long dictId);

	/**
	 * 新增
	 */
	void add(DictInfoDto sysDict);

	/**
	 * 删除
	 */
	void delete(List<Integer> dictIds);

	/**
	 * 修改
	 */
	void edit(DictInfoDto sysDict);

	/**
	 * 获取字典
	 */
	SysDict getByCode(String dictCode);

	/**
	 * 获取类型字典
	 */
	List<SysDict> listByType(String typeCode);

	/**
	 * 获取分组字典
	 */
	List<SysDict> listByGroup(String groupCode);

	/**
	 * 获取分组选项
	 */
	Collection<SelectOption> listTypeByGroup(String groupCode);
}
