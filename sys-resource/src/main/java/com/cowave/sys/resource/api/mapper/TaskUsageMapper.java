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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface TaskUsageMapper {

    /**
     * 任务最新usageId
     */
    Long latestUsageId(String taskId);

    /**
     * 资源使用记录
     */
    List<ResourceUsage> list(@Param("taskId") String taskId, @Param("usageId") Long usageId);

    /**
     * 历史使用记录
     */
    List<ResourceUsage> historyList(String taskId);

    /**
     * 任务释放资源
     */
    int statusForward(@Param("taskId") String taskId, @Param("usageId") Long usageId,
                      @Param("status") Integer status, @Param("user") AccessUser user);

    /**
     * 转为历史记录
     */
    int moveHistory(String taskId);

    /**
     * 清除记录
     */
    void clean(String taskId);

    /**
     * 检查释放资源池
     */
    void checkReleasePool(String taskId);

}
