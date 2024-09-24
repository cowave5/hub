/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.entity.task;

import com.cowave.commons.framework.access.AccessUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TaskUsage extends AccessUser {

    /**
     * 使用id
     **/
    private Long usageId;

    /**
     * 任务id
     */
    @NotNull(message = "taskId不能为空")
    private String taskId;

    /**
     * 任务名称
     */
    @NotNull(message = "taskName不能为空")
    private String taskName;

    /**
     * 子网id
     */
    @NotNull(message = "netNo不能为空")
    private String netNo;

    /**
     * 资源需求
     */
    @Valid
    @NotEmpty(message = "usages不能为空")
    private List<Usage> usages;
}
