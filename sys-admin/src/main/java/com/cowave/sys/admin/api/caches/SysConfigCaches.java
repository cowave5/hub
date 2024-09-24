/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.caches;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessLogger;
import com.cowave.commons.framework.helper.dict.DictValueParser;
import com.cowave.commons.framework.support.redis.RedisHelper;
import com.cowave.sys.admin.api.mapper.SysConfigMapper;
import com.cowave.sys.model.admin.SysConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysConfigCaches implements ApplicationRunner {

    public static final String KEY_CONFIG = "sys-config:";

    private final Map<String, DictValueParser> parserMap = new ConcurrentHashMap<>();

    private final RedisHelper redisHelper;

    private final SysConfigMapper sysConfigMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    public void refresh() throws Exception {
        AccessLogger.info("refresh cache of config ...");
        Collection<String> keys = redisHelper.keys(KEY_CONFIG + "*");
        redisHelper.delete(keys);
        List<SysConfig> list = sysConfigMapper.list(Access.page(Integer.MAX_VALUE), new SysConfig()).getRecords();
        for(SysConfig conf : list) {
            putValue(conf);
        }
    }

    public <T> T getValue(String configKey) {
        return redisHelper.getValue(KEY_CONFIG + configKey);
    }

    public void putValue(SysConfig conf) throws Exception {
        String parserClazz = conf.getValueParser();
        if(StringUtils.isBlank(parserClazz)){
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), conf.getConfigValue());
        }else{
            DictValueParser parser = parserMap.get(parserClazz);
            if(parser == null){
                parser = (DictValueParser)Class.forName(parserClazz).getDeclaredConstructor().newInstance();
                parserMap.put(parserClazz, parser);
            }
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), parser.parse(conf.getConfigValue(), conf.getValueParam()));
        }
    }

    public void delete(SysConfig conf) {
        redisHelper.delete(KEY_CONFIG + conf.getConfigKey());
    }
}
