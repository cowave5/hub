/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.commons.framework.access.AccessUser;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.Usage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface UsageMapper {

    /**
     * 下一个usageId
     */
    Long nextUsageId();

    /**
     * id查询
     */
    ResourceUsage queryById(Long id);

    /**
     * 转发器资源使用记录
     */
    List<ResourceUsage> queryByTspdId(Integer tspdId);

    /**
     * 转发器指定时间内使用记录
     */
    List<ResourceUsage> queryByTspdIdWithTime(Usage usage);

    /**
     * 使用记录
     */
    List<ResourceUsage> list(Long usageId);

    /**
     * 冲突记录
     */
    List<ResourceUsage> conflictList(@Param("usage") Usage usage, @Param("taskId") String taskId, @Param("netNo") String netNo);

    /**
     * 创建使用记录
     */
    void createUsage(@Param("usage") ResourceUsage usage, @Param("user") AccessUser user);

    /**
     * 创建合并记录
     */
    void createMergeUsage(ResourceUsage resourceUsage);

    /**
     * 更新mergeId
     */
    void updateMergeId(@Param("list") List<Long> list, @Param("mergeId") Long mergeId);
}
