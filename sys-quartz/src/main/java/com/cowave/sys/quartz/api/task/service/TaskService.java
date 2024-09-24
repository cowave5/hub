/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.service;

import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import org.quartz.SchedulerException;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface TaskService {

    /**
     * 重新加载
     */
    void refresh() throws SchedulerException;

    /**
     * 列表
     */
    List<QuartzTask> list(QuartzTask job);

    /**
     * 详情
     */
    QuartzTask info(Long id);

    /**
     * 新增
     */
    void add(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 修改
     */
    void edit(QuartzTask quartzTask) throws SchedulerException;

    /**
     * 删除
     */
    void delete(Long[] id) throws SchedulerException;

    /**
     * 立即执行
     */
    void exec(Long id) throws SchedulerException;

    /**
     * 状态修改
     */
    void changeStatus(Long id, Integer status) throws SchedulerException;
}
