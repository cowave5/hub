/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.quartz.api.qrtz.entity;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author shanhuiming
 */
@Data
public class QuartzDetail {

    /**
     * cron计划
     */
    private String cronExpression;

    /**
     * 作业实现类
     */
    private String jobClassName;

    /**
     * 作业分组
     */
    private String jobGroupName;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 触发器分组
     */
    private String triggerGroupName;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 下次触发时间
     */
    private Date nextFireTime;

    /**
     * 上次触发时间
     */
    private Date previousFireTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 时区
     */
    private String timeZone;

    /**
     * 状态
     */
    private String status;
}
