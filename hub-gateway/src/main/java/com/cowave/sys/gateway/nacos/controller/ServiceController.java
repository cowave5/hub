/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.nacos.controller;

import com.cowave.sys.gateway.nacos.model.Result;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealth;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealthOld;
import com.cowave.sys.gateway.nacos.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author github.com/liucheng
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ServiceController {

    private static final String CONSUL_IDX_HEADER = "X-Consul-Index";

    private static final String QUERY_PARAM_WAIT = "wait";
    private static final String QUERY_PARAM_INDEX = "index";

    private static final Pattern WAIT_PATTERN = Pattern.compile("(\\d*)(m|s|ms|h)");
    private static final Random RANDOM = new Random();

    private final RegistrationService registrationService;

    /**
     * 获取所有服务名称
     * @param wait 超时时间
     * @param index 版本号
     */
    @GetMapping(value = "/v1/catalog/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, List<String>>>> getServiceNames(
            @RequestParam(name = QUERY_PARAM_WAIT, required = false) String wait,
            @RequestParam(name = QUERY_PARAM_INDEX, required = false) Long index) {
        log.debug("receive request register service.");
        return registrationService.getServiceNames(getWaitMillis(wait), index)
                .map(item -> createResponseEntity(item));
    }

    /**
     * 获取指定服务实例情况（新版本）
     * @param appName 执行服务名称
     * @param wait 超时时间
     * @param index 版本号
     */
    @GetMapping(value = "/v1/health/service/{appName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ServiceInstancesHealth>>> getService(@PathVariable("appName") String appName,
                                                                         @RequestParam(name = QUERY_PARAM_WAIT, required = false) String wait,
                                                                         @RequestParam(name = QUERY_PARAM_INDEX, required = false) Long index) {
        Assert.isTrue(appName != null, "service name can not be null");
        log.debug("请求注册中心服务器：{}，wait:{},index:{}", appName, wait, index);
        return registrationService.getServiceInstancesHealth(appName, getWaitMillis(wait), index).map(item -> {
            return createResponseEntity(item);
        });
    }

    /**
     * 获取指定服务实例情况(老版本)
     * @param appName 执行服务名称
     * @param wait 超时时间
     * @param index 版本号
     */
    @GetMapping(value = "/v1/catalog/service/{appName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ServiceInstancesHealthOld>>> oldGetService(@PathVariable("appName") String appName,
                                                                               @RequestParam(name = QUERY_PARAM_WAIT, required = false) String wait,
                                                                               @RequestParam(name = QUERY_PARAM_INDEX, required = false) Long index) {
        Assert.isTrue(appName != null, "service name can not be null");
        log.debug("请求注册中心服务器：{}，wait:{},index:{}", appName, wait, index);
        return registrationService.getServiceInstancesHealthOld(appName, getWaitMillis(wait), index).map(item -> createResponseEntity(item));
    }

    private MultiValueMap<String, String> createHeaders(long index) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONSUL_IDX_HEADER, "" + index);
        return headers;
    }

    private <T> ResponseEntity createResponseEntity(Result result) {
        return new ResponseEntity<>(result.getData(), createHeaders(result.getChangeIndex()), HttpStatus.OK);
    }

    /**
     * Details to the wait behaviour can be found
     * https://www.consul.io/api/index.html#blocking-queries
     */
    private long getWaitMillis(String wait) {
        // default from consul docu
        long millis = TimeUnit.MINUTES.toMillis(5);
        if (wait != null) {
            Matcher matcher = WAIT_PATTERN.matcher(wait);
            if (matcher.matches()) {
                Long value = Long.valueOf(matcher.group(1));
                TimeUnit timeUnit = parseTimeUnit(matcher.group(2));
                millis = timeUnit.toMillis(value);
            } else {
                throw new IllegalArgumentException("Invalid wait pattern");
            }
        }
        return millis + RANDOM.nextInt(((int) millis / 16) + 1);
    }

    private TimeUnit parseTimeUnit(String unit) {
        switch (unit) {
            case "h":
                return TimeUnit.HOURS;
            case "m":
                return TimeUnit.MINUTES;
            case "s":
                return TimeUnit.SECONDS;
            case "ms":
                return TimeUnit.MILLISECONDS;
            default:
                throw new IllegalArgumentException("No valid time unit");
        }
    }
}
