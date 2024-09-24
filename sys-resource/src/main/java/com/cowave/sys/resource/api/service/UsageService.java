/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.commons.framework.access.AccessUser;
import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.Usage;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface UsageService {

    /**
     * 使用记录
     */
    List<ResourceUsage> list(Long usageId);

    /**
     * 冲突记录
     */
    List<ResourceUsage> conflictList(Usage usage);

    /**
     * 创建资源使用记录
     */
    ResourceUsage allocateUsage(ResourceUsage allocateUsage, Usage usage, String taskId, String netNo, AccessUser user);

    /**
     * 使用资源分片
     */
    List<Usage> usageSplit(List<ResourcePool> pools, Usage usage);
}
