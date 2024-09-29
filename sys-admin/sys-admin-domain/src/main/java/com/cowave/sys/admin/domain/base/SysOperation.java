/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base;

import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.helper.es.HitEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Data
public class SysOperation implements HitEntity {

    public static final String INDEX_NAME = "sys_operation_log";

    /**
     * 日志id
     */
    private String id;

    /**
     * 操作模块
     */
    private String opModule;

    /**
     * 操作类型
     */
    private String opType;

    /**
     * 操作动作
     */
    private String opAction;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opTime;

    /**
     * 操作状态
     */
    private Integer opStatus;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 访问url
     */
    private String url;

    /**
     * 请求内容
     */
    private Object request;

    /**
     * 操作描述
     */
    private String opDesc;

    /**
     * 请求内容
     */
    private Object opContent;

    /**
     * 响应内容
     */
    private Object response;

    /**
     * 访问信息
     */
    private AccessInfo access;

    public SysOperation(OperationInfo opInfo) {
        this.opModule = opInfo.getOpModule();
        this.opType = opInfo.getOpType();
        this.opAction = opInfo.getOpAction();
        this.opDesc = opInfo.getDesc();
        this.opContent = opInfo.getOpContent();
        this.opStatus = opInfo.isSuccess() ? 1 : 0;
    }
}
