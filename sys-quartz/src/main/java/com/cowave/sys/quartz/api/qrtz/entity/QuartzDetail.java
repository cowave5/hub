/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
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
