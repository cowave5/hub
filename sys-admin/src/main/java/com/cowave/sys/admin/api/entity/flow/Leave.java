/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
    private Long applyUser;

    /**
     * 请假人
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private Long deptApprover;
}
