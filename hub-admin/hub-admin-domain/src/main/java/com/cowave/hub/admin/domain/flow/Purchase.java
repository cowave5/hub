/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.flow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 采购流程
 *
 * @author shanhuiming
 */
@Data
public class Purchase {

    /**
     * 部门审批
     */
    public static final Integer STATUS_DEPT = 1;

    /**
     * 财务审批
     */
    public static final Integer STATUS_FINANCE = 2;

    /**
     * 总经理审批
     */
    public static final Integer STATUS_GENERAL = 3;

    /**
     * 审批驳回
     */
    public static final Integer STATUS_REJECT = 4;

    /**
     * 待付款
     */
    public static final Integer STATUS_CASHIER = 5;

    /**
     * 待收货
     */
    public static final Integer STATUS_RECEIVING = 6;

    /**
     * 已收货
     */
    public static final Integer STATUS_RECEIVED = 7;

    /**
     * 已撤销
     */
    public static final Integer STATUS_REVOCATED = 8;

    /**
     * id
     */
    private Long id;

    /**
     * 采购内容
     */
    private String content;

    /**
     * 总金额
     */
    private BigDecimal money;

    /**
     * 申请人
     */
    private Integer applyUser;

    /**
     * 申请人
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程状态
     */
    private Integer processStatus;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 节点办理人
     */
    private String processTaskUser;

    /**
     * 采购审批人
     */
    private Integer manager;

    /**
     * 财务审批人
     */
    private Integer finance;

    /**
     * 出纳核实人
     */
    private Integer cashier;

    /**
     * 总经理审批人
     */
    private Integer general;
}
