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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.StringRedisHelper;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.domain.base.request.ConfigQuery;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysConfigDtoMapper;
import com.cowave.sys.admin.service.base.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;
import static com.cowave.sys.admin.domain.AdminRedisKeys.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysConfigServiceImpl implements SysConfigService {
    private final SysConfigDtoMapper sysConfigDtoMapper;
    private final SysConfigDao sysConfigDao;
    private final RedisHelper redisHelper;
    private final StringRedisHelper stringRedisHelper;

    @Override
    public Page<SysConfig> page(String tenantId, ConfigQuery query) {
        return sysConfigDao.page(tenantId, query);
    }

    @Override
    public List<SysConfig> list(String tenantId, ConfigQuery query) {
        return sysConfigDao.list(tenantId, query);
    }

    @Override
    public SysConfig info(String tenantId, Integer configId) {
        return sysConfigDao.getById(tenantId, configId);
    }

    @Override
    public void add(SysConfig sysConfig) {
        CustomValueParser.getValue(sysConfig.getConfigValue(), sysConfig.getValueType(), sysConfig.getValueParser());
        sysConfigDao.save(sysConfig);
    }

    @CacheEvict(value = CONFIG_KEY, key = "#sysConfig.tenantId + ':' + #sysConfig.configKey")
    @Override
    public void edit(SysConfig sysConfig) {
        HttpAsserts.notNull(sysConfig.getConfigId(), BAD_REQUEST, "{admin.config.id.null}");

        SysConfig preConfig = sysConfigDao.getById(sysConfig.getTenantId(), sysConfig.getConfigId());
        HttpAsserts.notNull(preConfig, NOT_FOUND, "{admin.config.not.exist}", sysConfig.getConfigId());

        // 校验parser
        CustomValueParser.getValue(sysConfig.getConfigValue(), sysConfig.getValueType(), sysConfig.getValueParser());
        sysConfigDao.updateConfig(sysConfig);
    }

    @Override
    public void delete(String tenantId, List<Integer> configIds) {
        List<SysConfig> list = sysConfigDao.listByIds(configIds);
        sysConfigDao.removeByIds(configIds);
        // 手动清除缓存
        for(SysConfig conf : list) {
            redisHelper.delete(CONFIG_KEY + ":" + tenantId + ":" + conf.getConfigKey());
        }
    }

    @Override
    public void resetConfig(String tenantId) {
        sysConfigDtoMapper.resetTenantConfig(tenantId);
        stringRedisHelper.luaClean(CONFIG_KEY + ":" + tenantId + ":");
    }

    @Override
    public <T> T getConfigValue(String tenantId, String configKey) {
        return sysConfigDao.getConfigValue(tenantId, configKey);
    }
}
