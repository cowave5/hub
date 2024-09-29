/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.gateway.nacos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author github.com/liucheng
 *
 */
@Getter
@ToString
public class ServiceInstancesHealthOld {

    @JsonProperty("ID")
    private String id;

    /**
     * nacos的节点名称
     */
    @JsonProperty("Node")
    private String node;

    /**
     * nacos的ip地址
     */
    @JsonProperty("Address")
    private String address;

    @JsonProperty("Datacenter")
    private String dataCenter;

    @JsonProperty("ServiceId")
    private String serviceId;

    @JsonProperty("ServiceName")
    private String serviceName;

    @JsonProperty("ServicePort")
    private Integer servicePort;

    @JsonProperty("ServiceAddress")
    private String serviceAddress;


    public ServiceInstancesHealthOld(ServiceInstancesHealth serviceInstancesHealth) {
        this.id = serviceInstancesHealth.getNode().getId();
        this.dataCenter = serviceInstancesHealth.getNode().getDataCenter();
        this.node = "nacos";
        this.address = serviceInstancesHealth.getNode().getAddress();

        this.serviceId = serviceInstancesHealth.getService().getId();
        this.serviceAddress = serviceInstancesHealth.getNode().getAddress();
        this.servicePort = serviceInstancesHealth.getService().getPort();
        this.serviceName = serviceInstancesHealth.getService().getService();
    }
}
