/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.redis;

import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.dict.CustomValueParser;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.infra.base.dao.SysConfigDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static com.cowave.sys.admin.domain.AdminRedisKeys.CONFIG_KEY;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysConfigRedis implements ApplicationRunner {
    private final RedisHelper redisHelper;
    private final SysConfigDao sysConfigDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of config ...");
        refreshConfig();
    }

    public void refreshConfig() {
        Collection<String> keys = redisHelper.keys(CONFIG_KEY.formatted("*"));
        redisHelper.delete(keys);
        List<SysConfig> list = sysConfigDao.list();
        for (SysConfig conf : list) {
            putConfig(conf);
        }
    }

    public void putConfig(SysConfig conf) {
        Object configValue = CustomValueParser.getValue(conf.getConfigValue(), conf.getValueType(), conf.getValueParser());
        redisHelper.putValue(CONFIG_KEY.formatted(conf.getConfigKey()), configValue);
    }

    public void removeConfig(SysConfig conf) {
        redisHelper.delete(CONFIG_KEY.formatted(conf.getConfigKey()));
    }

    public <T> T getConfigValue(String configKey) {
        return redisHelper.getValue(CONFIG_KEY.formatted(configKey));
    }
}
