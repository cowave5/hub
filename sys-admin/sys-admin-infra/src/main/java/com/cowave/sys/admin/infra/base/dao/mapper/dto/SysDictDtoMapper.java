/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao.mapper.dto;

import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysDictDtoMapper {

	/**
	 * 列表
	 */
	List<DictInfoDto> queryList(@Param("dictCode") String dictCode, @Param("dictName") String dictName, @Param("lang") String lang);

	/**
	 * 列表
	 */
	List<DictInfoDto> queryAll(@Param("groupCode") String groupCode, @Param("typeCode") String typeCode);

	/**
	 * 列表
	 */
	List<DictInfoDto> queryByParentCode(String parentCode);

	/**
	 * 列表
	 */
	List<DictInfoDto> queryByIds(List<Integer> list);

	/**
	 * 详情
	 */
	DictInfoDto getById(long id);

	/**
	 * 删除分组
	 */
	void deleteGroup(String groupCode);

	/**
	 * 字典类型选项
	 */
	List<SelectOption> dictOptions();

	/**
	 * Group子类型选项
	 */
	List<SelectOption> groupOptions(String groupCode);
}
