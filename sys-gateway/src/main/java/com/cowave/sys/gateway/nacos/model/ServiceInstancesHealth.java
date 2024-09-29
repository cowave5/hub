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
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 *
 * @author github.com/liucheng
 *
 */
@Builder
@Getter
@ToString
public class ServiceInstancesHealth {
    @JsonProperty("Node")
    private Node node;

    @JsonProperty("Service")
    private Service service;

    @JsonProperty("Checks")
    private List<Check> checks;

    @Getter
    @Builder
    public static class Node {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Address")
        //nacos默认注册为内网IP
        private String address;

        @JsonProperty("Datacenter")
        private String dataCenter;
    }

    @Getter
    @Builder
    public static class Service {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Service")
        private String service;

        @JsonProperty("Port")
        private int port;

        @JsonProperty("Meta")
        private Map<String, String> meta;
    }

    @Getter
    @Builder
    public static class Check {

        @JsonProperty("Node")
        private String node;

        @JsonProperty("CheckID")
        private String checkId;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Status")
        private String status;
    }
}
