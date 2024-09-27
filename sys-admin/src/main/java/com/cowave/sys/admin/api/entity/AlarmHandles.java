/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class AlarmHandles {

    /**
     * 告警id
     */
    @NotNull(message = "{valid.notnull.alarm.id}")
    private Long id;

    /**
     * 告警状态
     */
    @NotNull(message = "{valid.notnull.alarm.status}")
    private Integer alarmStatus;

    /**
     * 处理方式
     */
    private int handleType = 1;

    /**
     * 处理意见
     */
    private String handleMsg;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime = new Date();
}
