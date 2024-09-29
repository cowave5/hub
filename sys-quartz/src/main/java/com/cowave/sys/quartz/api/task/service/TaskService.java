/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
