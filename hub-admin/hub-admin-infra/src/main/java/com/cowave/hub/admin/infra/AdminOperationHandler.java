/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationHandler;
import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.helper.es.EsHelper;
import com.cowave.hub.admin.domain.sys.HubOperation;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class AdminOperationHandler implements OperationHandler {

    private static final String MAPPING_PROPERTIES = """
            {
                "mappings": {
                    "properties": {
                        "opTime": {
                            "type": "date",
                            "format": "yyyy-MM-dd HH:mm:ss||epoch_millis"
                        }
                    }
                }
            }
            """;

    private final EsHelper esHelper;

    @PostConstruct
    public void indexInit(){
        esHelper.indexCreate(HubOperation.INDEX_NAME, MAPPING_PROPERTIES);
        esHelper.indexSetting(HubOperation.INDEX_NAME, Settings.builder().put("index.max_result_window", 25000));
    }

    @Override
    public void handle(OperationInfo opInfo, Map<String, Object> args, Object resp, Exception e) {
        create(opInfo, resp);
    }

    public void create(OperationInfo opInfo, Object resp) {
        HubOperation hubOperation = new HubOperation(opInfo);
        if(hubOperation.getAccess() == null){
            hubOperation.setAccess(Access.accessInfo());
        }
        hubOperation.setIp(Access.accessIp());
        hubOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        hubOperation.setOpTime(Access.accessTime());
        // 请求内容
        hubOperation.setRequest(Access.getRequestParam());
        // 响应内容
        hubOperation.setResponse(resp);
        create(hubOperation);
    }

    public void create(HubOperation hubOperation) {
        esHelper.insert(HubOperation.INDEX_NAME, hubOperation);
    }
}
