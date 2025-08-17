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

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.cowave.sys.gateway.nacos.NacosConsulAdapterProperties;
import com.cowave.sys.gateway.nacos.NacosServiceCenter;
import com.cowave.sys.gateway.nacos.model.Result;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealth;
import com.cowave.sys.gateway.nacos.model.ServiceInstancesHealthOld;
import com.cowave.sys.gateway.nacos.service.RegistrationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author github.com/liucheng
 *
 */
@Slf4j
public class PollingRegistrationServiceImpl implements RegistrationService, ApplicationRunner {

    private NacosServiceCenter nacosServiceCenter;
    private NacosConsulAdapterProperties nacosConsulAdapterProperties;
    private DiscoveryClient discoveryClient;
    private ScheduledExecutorService executorService;
    private NacosServiceManager nacosServiceManager;
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    private NacosNamingService namingService;


    public PollingRegistrationServiceImpl(NacosConsulAdapterProperties nacosConsulAdapterProperties,
                                          DiscoveryClient discoveryClient, NacosServiceManager nacosServiceManager,
                                          NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosConsulAdapterProperties = nacosConsulAdapterProperties;
        this.discoveryClient = discoveryClient;
        executorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread t = new Thread(r);
            t.setName("at.liucheng.nacos-consul-adapter.service.updater");
            t.setDaemon(true);
            return t;
        });
        this.nacosServiceManager = nacosServiceManager;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        namingService = (NacosNamingService) nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
        nacosServiceCenter = new NacosServiceCenter(namingService, nacosDiscoveryProperties);
    }

    @Override
    public Mono<Result<Map<String, List<Object>>>> getServiceNames(long waitMills, Long index) {
        return Mono.just(nacosServiceCenter.getServiceNames())
                .map(serviceSet -> {
                    Map<String, List<Object>> result = new HashMap<>(serviceSet.size());
                    for (String item : serviceSet) {
                        result.put(item, Collections.emptyList());
                    }
                    return result;
                })
                .map(data -> new Result<>(data, System.currentTimeMillis()));
    }


    @Override
    public Mono<Result<List<ServiceInstancesHealth>>> getServiceInstancesHealth(String serviceName, long waitMillis, Long index) {
        Long version = nacosServiceCenter.getServiceVersion(serviceName);
        //如果index和现在的version不同（只有可能是index 小于version）,说明服务发生了变动马上返回。
        if (index == null || !index.equals(version)) {
            log.debug("{} had changed,direct return.", serviceName);
            return Mono.just(new Result<>(getServiceInstance(serviceName), version));
        }

        return nacosServiceCenter.getChangeHotSource(serviceName)
                .map(result -> result.getChangeIndex())
                .timeout(Duration.ofMillis(waitMillis), Flux.just(version))
                .take(1)
                .collectList()
                .map(newVersionList -> {
                    Long newVersion = newVersionList.get(0);
                    if (!version.equals(newVersion)) {
                        log.debug("during long-polling,{} had changed.version is {}", serviceName, newVersion);
                    } else {
                        log.debug("during long-polling,{} not changed.version is {}", serviceName, newVersion);
                    }
                    return new Result<>(getServiceInstance(serviceName), newVersion);
                });
    }


    @Override
    public Mono<Result<List<ServiceInstancesHealthOld>>> getServiceInstancesHealthOld(String serviceName, long waitMillis, Long index) {
        Long version = nacosServiceCenter.getServiceVersion(serviceName);
        //如果index和现在的version不同（只有可能是index 小于version）,说明服务发生了变动马上返回。
        if (index == null || !index.equals(version)) {
            log.debug("{} had changed,direct return.", serviceName);
            return Mono.just(new Result<>(getServiceInstanceOld(serviceName), version));
        }
        return nacosServiceCenter.getChangeHotSource(serviceName)
                .map(result -> result.getChangeIndex())
                .timeout(Duration.ofMillis(waitMillis), Flux.just(version))
                .take(1)
                .collectList()
                .map(newVersionList -> {
                    Long newVersion = newVersionList.get(0);
                    if (!version.equals(newVersion)) {
                        log.debug("during long-polling,{} had changed.version is {}", serviceName, newVersion);
                    } else {
                        log.debug("during long-polling,{} not changed.version is {}", serviceName, newVersion);
                    }
                    return new Result<>(getServiceInstanceOld(serviceName), newVersion);
                });
    }

    @SneakyThrows
    private List<ServiceInstancesHealth> getServiceInstance(String serviceName) {
        return namingService.selectInstances(serviceName, nacosDiscoveryProperties.getGroup(), true).stream().map(instance -> {
            ServiceInstancesHealth.Node node = ServiceInstancesHealth.Node.builder()
                    .address(instance.getIp())
                    .id(instance.getInstanceId())
                    //todo 数据中心
                    .dataCenter("dc1")
                    .build();

            Map<String, String> metadataMap = instance.getMetadata();
            metadataMap.put(NACOS_APPLICATION_NAME, serviceName);
            log.info("meta1: " + metadataMap);

            ServiceInstancesHealth.Service service = ServiceInstancesHealth.Service.builder()
                    .service(serviceName)
                    .id(serviceName + "-" + instance.getPort())
                    .port(instance.getPort())
                    .meta(metadataMap)
                    .build();
            return ServiceInstancesHealth.builder().node(node).service(service).build();
        }).collect(Collectors.toList());
    }


    @SneakyThrows
    private List<ServiceInstancesHealthOld> getServiceInstanceOld(String serviceName) {
        return namingService.selectInstances(serviceName, nacosDiscoveryProperties.getGroup(), true).stream().map(instance -> {
            ServiceInstancesHealth.Node node = ServiceInstancesHealth.Node.builder()
                    .address(instance.getIp())
                    .id(instance.getInstanceId())
                    //todo 数据中心
                    .dataCenter("dc1")
                    .build();
            ServiceInstancesHealth.Service service = ServiceInstancesHealth.Service.builder()
                    .service(serviceName)
                    .id(serviceName + "-" + instance.getPort())
                    .port(instance.getPort())
                    .build();
            return ServiceInstancesHealth.builder().node(node).service(service).build();
        }).map(serviceInstancesHealth -> new ServiceInstancesHealthOld(serviceInstancesHealth))
                .collect(Collectors.toList());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        nacosServiceCenter.initSetNames(discoveryClient.getServices());

        this.executorService.scheduleWithFixedDelay(() -> {
            nacosServiceCenter.setServiceNames(discoveryClient.getServices());
        }, 0, nacosConsulAdapterProperties.getServiceNameIntervalMills(), TimeUnit.MILLISECONDS);

    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }
}
