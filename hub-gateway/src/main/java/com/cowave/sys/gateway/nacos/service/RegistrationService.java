/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.nacos.service;

import com.cowave.sys.gateway.nacos.model.Result;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealth;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealthOld;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 *
 * @author github.com/liucheng
 *
 */
public interface RegistrationService {

    //Prometheus自定义标签
    String NACOS_APPLICATION_NAME = "nacos_application_name";

    /**
     * get all service name
     * 获取所有服务名称
     *
     * @param waitMills
     * @param index
     * @return
     */
    Mono<Result<Map<String, List<Object>>>> getServiceNames(long waitMills, Long index);

    /**
     * get speci service all instance
     * 获取指定服务所有实例
     *
     * @param serviceName
     * @param waitMillis
     * @param index
     * @return
     */
    Mono<Result<List<ServiceInstancesHealth>>> getServiceInstancesHealth(String serviceName, long waitMillis, Long index);

    /**
     * 获取指定服务所有实例 老版
     * @param serviceName
     * @param waitMillis
     * @param index
     * @return
     */
    Mono<Result<List<ServiceInstancesHealthOld>>> getServiceInstancesHealthOld(String serviceName, long waitMillis, Long index);

}
