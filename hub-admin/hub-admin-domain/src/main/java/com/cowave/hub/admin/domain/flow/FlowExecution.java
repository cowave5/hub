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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lockTime;
}
