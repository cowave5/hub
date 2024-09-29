/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.nacos.service.impl;

import com.cowave.sys.gateway.nacos.model.Result;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealth;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealthOld;
import com.cowave.sys.gateway.nacos.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 *
 * @author github.com/liucheng
 *
 */
@RequiredArgsConstructor
@Slf4j
public class DirectRegistrationServiceImpl implements RegistrationService {

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;

    @Override
    public Mono<Result<Map<String, List<Object>>>> getServiceNames(long waitMillis, Long index) {
        return reactiveDiscoveryClient.getServices()
                .collectList()
                .switchIfEmpty(Mono.just(Collections.emptyList()))
                .map(serviceList -> {
                    Set<String> set = new HashSet<>();
                    set.addAll(serviceList);
                    Map<String, List<Object>> result = new HashMap<>(serviceList.size());
                    for (String item : set) {
                        result.put(item, Collections.emptyList());
                    }
                    return result;
                })
                .map(data -> new Result<>(data, System.currentTimeMillis()));
    }

    @Override
    public Mono<Result<List<ServiceInstancesHealth>>> getServiceInstancesHealth(String serviceName, long waitMillis, Long index) {
        return reactiveDiscoveryClient.getInstances(serviceName)
                .map(serviceInstance -> {
                    ServiceInstancesHealth.Node node = ServiceInstancesHealth.Node.builder()
                            .address(serviceInstance.getHost())
                            .id(serviceInstance.getInstanceId())
                            //todo 数据中心
                            .dataCenter("dc1")
                            .build();

                    Map<String, String> metadataMap = serviceInstance.getMetadata();
                    metadataMap.put(NACOS_APPLICATION_NAME, serviceName);
                    log.info("meta2: " + metadataMap);

                    ServiceInstancesHealth.Service service = ServiceInstancesHealth.Service.builder()
                            .service(serviceInstance.getServiceId())
                            .id(serviceInstance.getServiceId() + "-" + serviceInstance.getPort())
                            .port(serviceInstance.getPort())
                            .meta(metadataMap)
                            .build();
                    return ServiceInstancesHealth.builder().node(node).service(service).build();
                })
                .collectList()
                .map(data -> new Result<>(data, System.currentTimeMillis()));
    }


    @Override
    public Mono<Result<List<ServiceInstancesHealthOld>>> getServiceInstancesHealthOld(String serviceName, long waitMillis, Long index) {
        return reactiveDiscoveryClient.getInstances(serviceName)
                .map(serviceInstance -> {
                    ServiceInstancesHealth.Node node = ServiceInstancesHealth.Node.builder()
                            .address(serviceInstance.getHost())
                            .id(serviceInstance.getInstanceId())
                            //todo 数据中心
                            .dataCenter("dc1")
                            .build();
                    ServiceInstancesHealth.Service service = ServiceInstancesHealth.Service.builder()
                            .service(serviceInstance.getServiceId())
                            .id(serviceInstance.getServiceId() + "-" + serviceInstance.getPort())
                            .port(serviceInstance.getPort())
                            .build();
                    return ServiceInstancesHealth.builder().node(node).service(service).build();
                })
                .map(serviceInstancesHealth -> new ServiceInstancesHealthOld(serviceInstancesHealth))
                .collectList()
                .map(data -> new Result<>(data, System.currentTimeMillis()));
    }


}
