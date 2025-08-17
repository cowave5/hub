/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.hub.admin.domain.sys.HubConfig;
import com.cowave.hub.admin.domain.sys.request.ConfigQuery;
import com.cowave.hub.admin.infra.sys.mapper.HubConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.CONFIG_KEY;

/**
 * @author shanhuiming
 */
@Repository
public class HubConfigDao extends ServiceImpl<HubConfigMapper, HubConfig> {

    /**
     * 获取配置
     */
    @Cacheable(value = CONFIG_KEY, key = "#tenantId + ':' + #configKey")
    public <T> T getConfigValue(String tenantId, String configKey) {
        HubConfig config = lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .eq(HubConfig::getConfigKey, configKey)
                .one();
        if(config == null){
            return null;
        }
        return (T) CustomValueParser.getValue(config.getConfigValue(), config.getValueType(), config.getValueParser());
    }

    /**
     * 分页查询
     */
    public Page<HubConfig> page(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, HubConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, HubConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), HubConfig::getConfigName, query.getConfigName())
                .orderByAsc(HubConfig::getConfigId)
                .page(Access.page());
    }

    /**
     * 列表
     */
    public List<HubConfig> list(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, HubConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, HubConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), HubConfig::getConfigName, query.getConfigName())
                .orderByAsc(HubConfig::getConfigId)
                .list();
    }

    /**
     * 查询（配置id）
     */
    public HubConfig getById(String tenantId, Integer configId) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .eq(HubConfig::getConfigId, configId)
                .one();
    }

    /**
     * 更新配置
     */
    public void updateConfig(HubConfig hubConfig){
        lambdaUpdate()
                .eq(HubConfig::getConfigId, hubConfig.getConfigId())
                .set(HubConfig::getConfigName, hubConfig.getConfigName())
                .set(HubConfig::getConfigKey, hubConfig.getConfigKey())
                .set(HubConfig::getConfigValue, hubConfig.getConfigValue())
                .set(HubConfig::getValueParser, hubConfig.getValueParser())
                .set(HubConfig::getValueType, hubConfig.getValueType())
                .set(HubConfig::getIsDefault, hubConfig.getIsDefault())
                .set(HubConfig::getRemark, hubConfig.getRemark())
                .set(HubConfig::getUpdateBy, Access.userCode())
                .set(HubConfig::getUpdateTime, new Date())
                .update();
    }
}
