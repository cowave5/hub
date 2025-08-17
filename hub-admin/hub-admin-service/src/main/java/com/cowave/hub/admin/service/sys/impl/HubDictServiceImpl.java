/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys.impl;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.StringRedisHelper;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.hub.admin.domain.sys.HubDict;
import com.cowave.hub.admin.domain.sys.dto.DictInfoDto;
import com.cowave.hub.admin.domain.sys.request.DictQuery;
import com.cowave.hub.admin.domain.sys.vo.SelectOption;
import com.cowave.hub.admin.infra.sys.dao.HubDictDao;
import com.cowave.hub.admin.infra.sys.mapper.dto.HubDictDtoMapper;
import com.cowave.hub.admin.service.sys.HubDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.commons.framework.access.security.Permission.TENANT_SYSTEM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubDictServiceImpl implements HubDictService {

    private final StringRedisHelper redisHelper;

    private final HubDictDao hubDictDao;

    private final HubDictDtoMapper hubDictDtoMapper;

    @Override
    public List<DictInfoDto> queryList(DictQuery query) {
        return hubDictDtoMapper.queryList(query.getDictCode(), query.getDictName());
    }

    @Override
    public DictInfoDto info(Long dictId) {
        return hubDictDtoMapper.getById(dictId);
    }

    @Override
    public void add(DictInfoDto dict) {
        // 校验字典值
        CustomValueParser.getValue(dict.getDictValue(), dict.getValueType(), dict.getValueParser());
        // 保存字典
        dict.setParentCode(dict.getTypeCode());
        hubDictDao.save(dict);
        // 更新缓存
        DictInfoDto dictInfo = hubDictDtoMapper.getById(dict.getId());
        redisHelper.delete(DICT_TYPE + ":" + dictInfo.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + dictInfo.getGroupCode());
    }

    @Override
    public void delete(List<Integer> dictIds) {
        List<DictInfoDto> list = hubDictDtoMapper.queryByIds(dictIds);
        hubDictDao.removeByIds(dictIds);
        for (DictInfoDto dict : list) {
            if ("root".equals(dict.getGroupCode())) {
                hubDictDtoMapper.removeByGroup(dict.getDictCode());
            } else if ("group".equals(dict.getGroupCode())) {
                hubDictDao.removeByType(dict.getDictCode());
            }
        }
        // 清空缓存
        redisHelper.luaClean("hub-admin:dict:*");
    }

    @Override
    public void edit(DictInfoDto dict) {
        HttpAsserts.notNull(dict.getId(), BAD_REQUEST, "{admin.dict.id.null}");

        // 校验字典值
        CustomValueParser.getValue(dict.getDictValue(), dict.getValueType(), dict.getValueParser());

        DictInfoDto preDict = hubDictDtoMapper.getById(dict.getId());
        HttpAsserts.notNull(preDict, NOT_FOUND, "{admin.dict.not.exist}", dict.getId());

        hubDictDao.updateDict(dict);
        DictInfoDto newDict = hubDictDtoMapper.getById(dict.getId());

        // 更新下级字典
        if ("root".equals(preDict.getGroupCode()) || "group".equals(preDict.getGroupCode())) {
            hubDictDao.updateParentCode(newDict.getDictCode(), preDict.getDictCode());
        }
        // 更新缓存
        redisHelper.delete(DICT_CODE + ":" + preDict.getDictCode());
        redisHelper.delete(DICT_TYPE + ":" + preDict.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + preDict.getGroupCode());
    }

    @Cacheable(value = DICT_CODE, key = "#dictCode")
    @Override
    public HubDict getByCode(String dictCode) {
        HubDict dict = hubDictDao.getByCode(dictCode);
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
    public List<HubDict> listByType(String typeCode) {
        List<HubDict> list = hubDictDao.listByType(typeCode);
        if(list.isEmpty()){
            return list;
        }
        for(HubDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Cacheable(value = DICT_GROUP, key = "#groupCode")
    @Override
    public List<HubDict> listByGroup(String groupCode) {
        List<HubDict> list = hubDictDtoMapper.listByGroup(groupCode);
        if(list.isEmpty()){
            return list;
        }
        for(HubDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Override
    public Collection<SelectOption> listTypeByGroup(String groupCode) {
        List<DictInfoDto> list = hubDictDtoMapper.listTypeByGroup(groupCode);
        Map<String, SelectOption> map = new LinkedHashMap<>();
        for (DictInfoDto infoDto : list) {
            if ("domain_module".equals(groupCode) && !TENANT_SYSTEM.equals(Access.tenantId())
                    && (SYSTEM_TENANT.equals(infoDto.getTypeCode())
                            || SYSTEM_MENU.equals(infoDto.getTypeCode())
                            || SYSTEM_DICT.equals(infoDto.getTypeCode())
                            || SYSTEM_ATTACH.equals(infoDto.getTypeCode()))
            ) {
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
