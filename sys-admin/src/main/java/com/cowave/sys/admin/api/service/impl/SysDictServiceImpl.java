/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.caches.SysDictCaches;
import com.cowave.sys.admin.api.entity.SelectOption;
import com.cowave.sys.admin.api.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cowave.sys.admin.api.mapper.SysDictMapper;
import com.cowave.sys.model.admin.SysDict;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysDictServiceImpl implements SysDictService {

	private final SysDictCaches sysDictCaches;

	private final SysDictMapper sysDictMapper;

	@Override
	public List<SelectOption> dictOptions() {
		return sysDictMapper.dictOptions();
	}

	@Override
	public List<SelectOption> groupOptions(String groupCode) {
		return sysDictMapper.groupOptions(groupCode);
	}

	@Override
	public Page<SysDict> list(String groupCode, String typeCode){
		return sysDictMapper.list(Access.page(), groupCode, typeCode);
	}

	@Override
	public SysDict info(Long dictId){
		return sysDictMapper.queryById(dictId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(SysDict sysDict){
		sysDictMapper.insert(sysDict);
		sysDict = sysDictMapper.queryById(sysDict.getId()); // group/type
		sysDictCaches.parseValue(sysDict);
		sysDictCaches.put(sysDict);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(SysDict sysDict){
		Asserts.notNull(sysDict.getId(), "dict.notnull.id");
		SysDict preDict = sysDictMapper.queryById(sysDict.getId());
		Asserts.notNull(preDict, "dict.notexist", sysDict.getId());
		Asserts.notEquals(1, preDict.getReadOnly(), "dict.forbid.edit.readonly");
		sysDictMapper.update(sysDict);
		sysDictCaches.parseValue(sysDict);
		// 组字典
		if("dict_root".equals(preDict.getGroupCode()) && !preDict.getDictCode().equals(sysDict.getDictCode())){
			sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
			sysDictCaches.removeGroup(preDict.getDictCode());
			List<SysDict> list = sysDictMapper.list(Access.page(Integer.MAX_VALUE), sysDict.getDictCode(), null).getRecords();
			list.addAll(sysDictMapper.queryByParentCode(sysDict.getDictCode()));
			for(SysDict dict : list){
				sysDictCaches.put(dict);
			}
		}
		// 类型字典
		if(preDict.getGroupCode().equals("dict_group") && !preDict.getDictCode().equals(sysDict.getDictCode())){
			sysDictMapper.updateChildren(sysDict.getDictCode(), preDict.getDictCode());
			sysDictCaches.removeType(preDict.getDictCode());
			List<SysDict> list = sysDictMapper.list(Access.page(Integer.MAX_VALUE), null, sysDict.getDictCode()).getRecords();
			for(SysDict dict : list){
				sysDictCaches.put(dict);
			}
		}
		sysDictCaches.removeDict(preDict.getDictCode());
		sysDictCaches.put(sysDictMapper.queryById(sysDict.getId()));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(Long[] dictId){
		Asserts.equals(0, sysDictMapper.countReadOnlyByIdArray(dictId), "dict.forbid.delete.readonly");
		List<SysDict> list = sysDictMapper.queryByIdArray(dictId);
		for(SysDict dict : list){
			delete(dict);
		}
	}

	private void delete(SysDict dict){
		sysDictMapper.delete(dict.getId());
		sysDictCaches.removeDict(dict.getDictCode());
		// 组字典
		if(dict.getGroupCode().equals("dict_root")){
			sysDictMapper.deleteGroup(dict.getDictCode());
			sysDictCaches.removeGroup(dict.getDictCode());
		}
		// 类型字典
		if(dict.getGroupCode().equals("dict_group")){
			sysDictMapper.deleteType(dict.getDictCode());
			sysDictCaches.removeType(dict.getDictCode());
		}
	}

	@Override
	public void changeReadonly(SysDict sysDict) {
		sysDictMapper.changeReadonly(sysDict);
	}
}
