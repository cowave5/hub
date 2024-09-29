/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.flow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 请假流程
 *
 * @author shanhuiming
 */
@Data
public class Leave {

    /**
     * 已撤销
     */
    public static final Integer PROCESS_STATUS_REVOCATE = 0;

    /**
     * 已撤销
     */
    public static final Integer PROCESS_STATUS_APPROVING = 1;

    /**
     * 已审批
     */
    public static final Integer PROCESS_STATUS_APPROVED = 2;

    /**
     * id
     */
    private Long id;

    /**
     * 类型
     */
    private Integer leaveType;

    /**
     * 原因
     */
    private String reason;

    /**
     * 请假人
     */
    private Integer applyUser;

    /**
     * 请假人
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

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
     * 流程任务节点
     */
    private String processTask;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 节点办理人
     */
    private String processTaskUser;

    /**
     * 部门审批人
     */
    private Integer deptApprover;
}
