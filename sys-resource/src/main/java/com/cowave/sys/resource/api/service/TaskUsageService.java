/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.TaskUsage;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface TaskUsageService {

    /**
     * 资源使用记录
     */
    List<ResourceUsage> list(String taskId, boolean latest);

    /**
     * 历史使用记录
     */
    List<ResourceUsage> historyList(String taskId);

    /**
     * 申请资源
     */
    List<ResourceUsage> allocate(TaskUsage taskUsage);

    /**
     * 释放资源
     */
    void release(String taskId);

    /**
     * 调整资源
     */
    List<ResourceUsage> adjust(TaskUsage taskUsage);
}
