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

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.utils.NamingUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 *
 * @author github.com/liucheng
 *
 */
@Slf4j
public class ServiceChangeListener implements EventListener {
    private NacosServiceCenter nacosServiceCenter;
    private String serviceName;

    public ServiceChangeListener(String serviceName, NacosServiceCenter nacosServiceCenter) {
        this.nacosServiceCenter = nacosServiceCenter;
        this.serviceName = serviceName;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NamingEvent) {
            NamingEvent namingEvent = (NamingEvent) event;
            log.debug("receive {} service change event.", ((NamingEvent) event).getServiceName());
            nacosServiceCenter.publish(NamingUtils.getServiceName(namingEvent.getServiceName()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceChangeListener that = (ServiceChangeListener) o;
        return serviceName.equals(that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName);
    }
}
