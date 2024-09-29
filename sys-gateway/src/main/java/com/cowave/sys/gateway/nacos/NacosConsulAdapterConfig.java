/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.cowave.sys.gateway.nacos.controller.AgentController;
import com.cowave.sys.gateway.nacos.controller.ServiceController;
import com.cowave.sys.gateway.nacos.service.RegistrationService;
import com.cowave.sys.gateway.nacos.service.impl.DirectRegistrationServiceImpl;
import com.cowave.sys.gateway.nacos.service.impl.PollingRegistrationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 *
 * @author github.com/liucheng
 *
 */
@Slf4j
@EnableConfigurationProperties
@Configuration
public class NacosConsulAdapterConfig {

    @Bean
    @ConditionalOnMissingBean
    public NacosConsulAdapterProperties nacosConsulAdapterProperties() {
        return new NacosConsulAdapterProperties();
    }


    @Bean
    public RegistrationService registrationService(NacosConsulAdapterProperties nacosConsulAdapterProperties,
                                                   DiscoveryClient discoveryClient, NacosServiceManager nacosServiceManager,
                                                   NacosDiscoveryProperties nacosDiscoveryProperties, ReactiveDiscoveryClient reactiveDiscoveryClient) {
        if (NacosConsulAdapterProperties.DIRECT_MODE.equals(nacosConsulAdapterProperties.getMode())) {
            return new DirectRegistrationServiceImpl(reactiveDiscoveryClient);
        }
        return new PollingRegistrationServiceImpl(nacosConsulAdapterProperties, discoveryClient, nacosServiceManager, nacosDiscoveryProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public AgentController agentController() {
        return new AgentController();
    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("nacosConsulAdapterProperties")
    public ServiceController serviceController(RegistrationService registrationService) {
        return new ServiceController(registrationService);
    }

}
