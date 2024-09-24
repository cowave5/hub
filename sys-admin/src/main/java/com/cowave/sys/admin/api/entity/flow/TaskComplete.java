/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import lombok.Data;

import java.util.Map;

/**
 * 完成任务
 *
 * @author shanhuiming
 */
@Data
public class TaskComplete {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务变量
     */
    Map<String, Object> variables;

    /**
     * 任务备注
     */
    private String comment;
}
