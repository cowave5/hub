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
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.StringRedisHelper;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.sys.admin.domain.base.SysDict;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.domain.base.request.DictQuery;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import com.cowave.sys.admin.infra.base.dao.SysDictDao;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysDictDtoMapper;
import com.cowave.sys.admin.service.base.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.commons.framework.access.security.Permission.TENANT_SYSTEM;
import static com.cowave.sys.admin.domain.AdminRedisKeys.*;
import static com.cowave.sys.admin.domain.constants.OpModule.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl implements SysDictService {

    private final StringRedisHelper redisHelper;

    private final SysDictDao sysDictDao;

    private final SysDictDtoMapper sysDictMapper;

    @Override
    public List<DictInfoDto> queryList(DictQuery query) {
        return sysDictMapper.queryList(query.getDictCode(), query.getDictName());
    }

    @Override
    public DictInfoDto info(Long dictId) {
        return sysDictMapper.getById(dictId);
    }

    @Override
    public void add(DictInfoDto sysDict) {
        // 校验字典值
        CustomValueParser.getValue(sysDict.getDictValue(), sysDict.getValueType(), sysDict.getValueParser());
        // 保存字典
        sysDict.setParentCode(sysDict.getTypeCode());
        sysDictDao.save(sysDict);
        // 更新缓存
        DictInfoDto dictInfo = sysDictMapper.getById(sysDict.getId());
        redisHelper.delete(DICT_TYPE + ":" + dictInfo.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + dictInfo.getGroupCode());
    }

    @Override
    public void delete(List<Integer> dictIds) {
        List<DictInfoDto> list = sysDictMapper.queryByIds(dictIds);
        sysDictDao.removeByIds(dictIds);
        for (DictInfoDto dict : list) {
            if ("root".equals(dict.getGroupCode())) {
                sysDictMapper.removeByGroup(dict.getDictCode());
            } else if ("group".equals(dict.getGroupCode())) {
                sysDictDao.removeByType(dict.getDictCode());
            }
        }
        // 清空缓存
        redisHelper.luaClean("sys-admin:dict:*");
    }

    @Override
    public void edit(DictInfoDto sysDict) {
        HttpAsserts.notNull(sysDict.getId(), BAD_REQUEST, "{admin.dict.id.null}");

        // 校验字典值
        CustomValueParser.getValue(sysDict.getDictValue(), sysDict.getValueType(), sysDict.getValueParser());

        DictInfoDto preDict = sysDictMapper.getById(sysDict.getId());
        HttpAsserts.notNull(preDict, NOT_FOUND, "{admin.dict.not.exist}", sysDict.getId());

        sysDictDao.updateDict(sysDict);
        DictInfoDto newDict = sysDictMapper.getById(sysDict.getId());

        // 更新下级字典
        if ("root".equals(preDict.getGroupCode()) || "group".equals(preDict.getGroupCode())) {
            sysDictDao.updateParentCode(newDict.getDictCode(), preDict.getDictCode());
        }
        // 更新缓存
        redisHelper.delete(DICT_CODE + ":" + preDict.getDictCode());
        redisHelper.delete(DICT_TYPE + ":" + preDict.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + preDict.getGroupCode());
    }

    @Cacheable(value = DICT_CODE, key = "#dictCode")
    @Override
    public SysDict getByCode(String dictCode) {
        SysDict dict = sysDictDao.getByCode(dictCode);
        if(dict == null){
            return null;
        }
        Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
        dict.setDictValue(dictValue);
        return dict;
    }

    @Cacheable(value = DICT_TYPE, key = "#typeCode")
    @Override
    public List<SysDict> listByType(String typeCode) {
        List<SysDict> list = sysDictDao.listByType(typeCode);
        if(list.isEmpty()){
            return list;
        }
        for(SysDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Cacheable(value = DICT_GROUP, key = "#groupCode")
    @Override
    public List<SysDict> listByGroup(String groupCode) {
        List<SysDict> list = sysDictMapper.listByGroup(groupCode);
        if(list.isEmpty()){
            return list;
        }
        for(SysDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Override
    public Collection<SelectOption> listTypeByGroup(String groupCode) {
        List<DictInfoDto> list = sysDictMapper.listTypeByGroup(groupCode);
        Map<String, SelectOption> map = new LinkedHashMap<>();
        for (DictInfoDto infoDto : list) {
            if ("domain_module".equals(groupCode) && !TENANT_SYSTEM.equals(Access.tenantId()) && (
                    SYSTEM_TENANT.equals(infoDto.getTypeCode())
                            || SYSTEM_MENU.equals(infoDto.getTypeCode())
                            || SYSTEM_DICT.equals(infoDto.getTypeCode())
                            || SYSTEM_ATTACH.equals(infoDto.getTypeCode())
            )) {
                continue;
            }

            if (groupCode.equals(infoDto.getGroupCode())) {
                map.computeIfAbsent(infoDto.getTypeCode(),
                    k -> new SelectOption(infoDto.getTypeCode(), infoDto.getTypeName()));
                continue;
            }

            SelectOption option = map.computeIfAbsent(infoDto.getGroupCode(),
                    k -> new SelectOption(infoDto.getGroupCode(), infoDto.getGroupName()));

            List<SelectOption> children = option.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                option.setChildren(children);
            }
            children.add(new SelectOption(infoDto.getTypeCode(), infoDto.getTypeName()));
        }
        return map.values();
    }
}
