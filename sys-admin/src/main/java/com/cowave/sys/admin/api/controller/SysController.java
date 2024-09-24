/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.sys.admin.api.entity.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 系统接口
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sys")
public class SysController {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis缓存状态
     */
    @GetMapping(value = "/cache")
    public Response<CacheInfo> cache() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setInfo(info);
        cacheInfo.setDbSize(dbSize);

        assert commandStats != null;
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", removeStart(key));
            data.put("value", substringBetween(property));
            pieList.add(data);
        });
        cacheInfo.setCommandStats(pieList);
        return Response.success(cacheInfo);
    }

    private static String removeStart(String str) {
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank("cmdstat_")) {
            return str.startsWith("cmdstat_") ? str.substring("cmdstat_".length()) : str;
        } else {
            return str;
        }
    }

    private static String substringBetween(String str) {
        if (!ObjectUtils.allNotNull(str, "calls=", ",usec")) {
            return null;
        } else {
            int start = str.indexOf("calls=");
            if (start != -1) {
                int end = str.indexOf(",usec", start + "calls=".length());
                if (end != -1) {
                    return str.substring(start + "calls=".length(), end);
                }
            }
            return null;
        }
    }
}
