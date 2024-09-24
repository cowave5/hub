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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 流程执行实例
 *
 * @author shanhuiming
 */
@Data
public class FlowExecution {

    private String id;

    private Long rev;

    private String procInstId;

    private String businessKey;

    private String parentId;

    private String procDefId;

    private String superExec;

    private String rootProcInstId;

    private String actId;

    private Integer isActive;

    private Integer isConcurrent;

    private Integer isScope;

    private Integer isEventScope;

    private Integer isMiRoot;

    private Long suspensionState;

    private Long cachedEntState;

    private String tenantId;

    private String name;

    private String startUserId;

    private Integer isCountEnabled;

    private Long evtSubscrCount;

    private Long taskCount;

    private Long jobCount;

    private Long timerJobCount;

    private Long suspJobCount;

    private Long deadletterJobCount;

    private Long varCount;

    private Long idLinkCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lockTime;
}
