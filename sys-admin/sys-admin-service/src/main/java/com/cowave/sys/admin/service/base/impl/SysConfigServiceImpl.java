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
import com.cowave.commons.client.http.constants.HttpCode;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.domain.base.request.ConfigQuery;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import com.cowave.sys.admin.infra.base.redis.SysConfigRedis;
import com.cowave.sys.admin.service.base.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl implements SysConfigService {
    private final SysConfigRedis sysConfigRedis;
    private final SysConfigDao sysConfigDao;

    @Override
    public Page<SysConfig> queryPage(ConfigQuery query) {
        return sysConfigDao.queryPage(query);
    }

    @Override
    public List<SysConfig> queryList(ConfigQuery query) {
        return sysConfigDao.queryList(query);
    }

    @Override
    public SysConfig info(Integer configId) {
        return sysConfigDao.getById(configId);
    }

    @Override
    public void add(SysConfig sysConfig) throws Exception {
        sysConfigDao.save(sysConfig);
        sysConfigRedis.putConfig(sysConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysConfig sysConfig) throws Exception {
        HttpAsserts.notNull(sysConfig.getConfigId(), BAD_REQUEST, "{admin.config.id.notnull}");

        SysConfig preConfig = sysConfigDao.getById(sysConfig.getConfigId());
        HttpAsserts.notNull(preConfig, NOT_FOUND, "{admin.config.notexist}", sysConfig.getConfigId());

        // 校验parser
        sysConfigDao.updateConfig(sysConfig);
        sysConfigRedis.putConfig(sysConfig);
    }

    @Override
    public void delete(List<Integer> configIds) throws Exception {
        List<SysConfig> list = sysConfigDao.listByIds(configIds);
        sysConfigDao.removeByIds(configIds);
        for(SysConfig conf : list) {
            sysConfigRedis.removeConfig(conf);
        }
    }

    /**
     * 刷新缓存
     */
    @Override
    public void refreshConfig() throws Exception {
        sysConfigRedis.refreshConfig();
    }

    /**
     * 获取配置值
     */
    @Override
    public String getConfigValue(String configKey) {
        return sysConfigRedis.getConfigValue(configKey);
    }
}
