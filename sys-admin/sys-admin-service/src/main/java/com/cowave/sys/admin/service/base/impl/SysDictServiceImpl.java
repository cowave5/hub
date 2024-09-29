/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.I18Messages;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.domain.base.request.DictQuery;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import com.cowave.sys.admin.infra.base.SysDictHelper;
import com.cowave.sys.admin.infra.base.dao.SysDictDao;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysDictDtoMapper;
import com.cowave.sys.admin.service.base.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl implements SysDictService {

	private final SysDictHelper sysDictHelper;

	private final SysDictDao sysDictDao;

	private final SysDictDtoMapper sysDictMapper;

	@Override
	public List<DictInfoDto> queryList(DictQuery query){
		return sysDictMapper.queryList(
				query.getDictCode(), query.getDictName(), I18Messages.getLanguage().getLanguage());
	}

	@Override
	public DictInfoDto info(Long dictId){
		return sysDictMapper.getById(dictId);
	}

	@Override
	public void add(DictInfoDto sysDict){
		sysDict.setParentCode(sysDict.getTypeCode());
		sysDictDao.save(sysDict);
		// 更新缓存
		DictInfoDto dictInfo = sysDictMapper.getById(sysDict.getId());
		sysDictHelper.put(dictInfo);
	}

	@Override
	public void delete(List<Integer> dictIds) {
		List<DictInfoDto> list = sysDictMapper.queryByIds(dictIds);
		sysDictDao.removeByIds(dictIds);
		for (DictInfoDto dict : list) {
			sysDictHelper.removeDict(dict.getDictCode());
			// 组字典
			if ("root".equals(dict.getGroupCode())) {
				sysDictMapper.deleteGroup(dict.getDictCode());
				sysDictHelper.removeGroup(dict.getDictCode());
			}
			// 类型字典
			if ("group".equals(dict.getGroupCode())) {
				sysDictDao.deleteByParentCode(dict.getDictCode());
				sysDictHelper.removeType(dict.getDictCode());
			}
		}
	}

	@Override
	public void edit(DictInfoDto sysDict){
		HttpAsserts.notNull(sysDict.getId(), BAD_REQUEST, "{admin.dict.id.notnull}");

		DictInfoDto preDict = sysDictMapper.getById(sysDict.getId());
		HttpAsserts.notNull(preDict, NOT_FOUND, "{admin.dict.notexist}", sysDict.getId());

		sysDictDao.updateDict(sysDict);
		sysDictHelper.put(sysDictMapper.getById(sysDict.getId()));
		// 字典码有变化
		if (!preDict.getDictCode().equals(sysDict.getDictCode())) {
			sysDictHelper.removeDict(preDict.getDictCode());
			// 组字典
			if ("root".equals(preDict.getGroupCode())) {
				sysDictDao.updateParentCode(sysDict.getDictCode(), preDict.getDictCode());

				sysDictHelper.removeGroup(preDict.getDictCode());
				List<DictInfoDto> list = sysDictMapper.queryAll(sysDict.getDictCode(), null);
				list.addAll(sysDictMapper.queryByParentCode(sysDict.getDictCode()));
				for (DictInfoDto dict : list) {
					sysDictHelper.put(dict);
				}
			}
			// 类型字典
			if ("group".equals(preDict.getGroupCode())) {
				sysDictDao.updateParentCode(sysDict.getDictCode(), preDict.getDictCode());

				sysDictHelper.removeType(preDict.getDictCode());
				List<DictInfoDto> list = sysDictMapper.queryAll(null, sysDict.getDictCode());
				for (DictInfoDto dict : list) {
					sysDictHelper.put(dict);
				}
			}
		}
	}

	@Override
	public List<SelectOption> groupOptions(String groupCode) {
		return sysDictMapper.groupOptions(groupCode);
	}
}
