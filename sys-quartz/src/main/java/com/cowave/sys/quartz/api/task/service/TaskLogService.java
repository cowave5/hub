/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.service;

import com.cowave.sys.quartz.api.task.entity.QuartzLog;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface TaskLogService {

    /**
     * 新增
     */
    void add(QuartzLog quartzLog);

    /**
     * 列表
     */
    List<QuartzLog> list(QuartzLog jobLog);

    /**
     * 删除
     */
    void delete(Long[] id);

    /**
     * 清空
     */
    void clean();
}
