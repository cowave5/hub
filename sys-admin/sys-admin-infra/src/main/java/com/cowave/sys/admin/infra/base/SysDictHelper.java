/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base;

import com.cowave.commons.framework.helper.redis.dict.DictHelper;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysDictDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDictHelper implements ApplicationRunner {

    private static final String PREFIX = "sys-admin";

    private final DictHelper dictHelper;

    private final SysDictDtoMapper sysDictDtoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of dict ...");
        refresh();
    }

    public void refresh() {
        dictHelper.clear();
        List<DictInfoDto> list = sysDictDtoMapper.queryAll(null, null);
        for (DictInfoDto sysDict : list) {
            dictHelper.put(sysDict, PREFIX);
        }
    }

    public void put(DictInfoDto dictInfo) {
        if (dictInfo.getStatus() == 1) {
            dictHelper.put(dictInfo, PREFIX);
        }
    }

    public DictInfoDto getDict(String dictCode) {
        return dictHelper.getDict(dictCode, PREFIX);
    }

    public List<DictInfoDto> getType(String typeCode) {
        return dictHelper.getType(typeCode, PREFIX);
    }

    public List<DictInfoDto> getGroup(String groupCode) {
        return dictHelper.getGroup(groupCode, PREFIX);
    }

    public void removeDict(String dictCode) {
        dictHelper.removeDict(dictCode, PREFIX);
    }

    public void removeType(String typeCode) {
        dictHelper.removeType(typeCode, PREFIX);
    }

    public void removeGroup(String groupCode) {
        dictHelper.removeGroup(groupCode, PREFIX);
    }
}
