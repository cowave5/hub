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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.StringRedisHelper;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.hub.admin.domain.sys.HubConfig;
import com.cowave.hub.admin.domain.sys.request.ConfigQuery;
import com.cowave.hub.admin.infra.sys.dao.HubConfigDao;
import com.cowave.hub.admin.infra.sys.mapper.dto.HubConfigDtoMapper;
import com.cowave.hub.admin.service.sys.HubConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubConfigServiceImpl implements HubConfigService {
    private final HubConfigDtoMapper hubConfigDtoMapper;
    private final HubConfigDao hubConfigDao;
    private final RedisHelper redisHelper;
    private final StringRedisHelper stringRedisHelper;

    @Override
    public Page<HubConfig> page(String tenantId, ConfigQuery query) {
        return hubConfigDao.page(tenantId, query);
    }

    @Override
    public List<HubConfig> list(String tenantId, ConfigQuery query) {
        return hubConfigDao.list(tenantId, query);
    }

    @Override
    public HubConfig info(String tenantId, Integer configId) {
        return hubConfigDao.getById(tenantId, configId);
    }

    @Override
    public void add(HubConfig hubConfig) {
        CustomValueParser.getValue(hubConfig.getConfigValue(), hubConfig.getValueType(), hubConfig.getValueParser());
        hubConfigDao.save(hubConfig);
    }

    @CacheEvict(value = CONFIG_KEY, key = "#hubConfig.tenantId + ':' + #hubConfig.configKey")
    @Override
    public void edit(HubConfig hubConfig) {
        HttpAsserts.notNull(hubConfig.getConfigId(), BAD_REQUEST, "{admin.config.id.null}");

        HubConfig preConfig = hubConfigDao.getById(hubConfig.getTenantId(), hubConfig.getConfigId());
        HttpAsserts.notNull(preConfig, NOT_FOUND, "{admin.config.not.exist}", hubConfig.getConfigId());

        // 校验parser
        CustomValueParser.getValue(hubConfig.getConfigValue(), hubConfig.getValueType(), hubConfig.getValueParser());
        hubConfigDao.updateConfig(hubConfig);
    }

    @Override
    public void delete(String tenantId, List<Integer> configIds) {
        List<HubConfig> list = hubConfigDao.listByIds(configIds);
        hubConfigDao.removeByIds(configIds);
        // 手动清除缓存
        for(HubConfig conf : list) {
            redisHelper.delete(CONFIG_KEY + ":" + tenantId + ":" + conf.getConfigKey());
        }
    }

    @Override
    public void resetConfig(String tenantId) {
        hubConfigDtoMapper.resetTenantConfig(tenantId);
        stringRedisHelper.luaClean(CONFIG_KEY + ":" + tenantId + ":");
    }

    @Override
    public <T> T getConfigValue(String tenantId, String configKey) {
        return hubConfigDao.getConfigValue(tenantId, configKey);
    }
}
