/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.helper.alarm.Alarm;
import com.cowave.commons.framework.support.excel.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 告警
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class SysAlarm implements Alarm {

	/**
	 * 告警id
	 */
	@ExcelProperty("告警id")
    private Long id;

    /**
     * 告警校验码
     */
    private String alarmCode;

    /**
     * 告警源id
     */
    private Long sourceId;

    /**
     * 告警源名称
     */
    private String sourceName;

    /**
     * 告警源类型
     */
    private Long sourceType;

    /**
     * 告警类型
     */
    private String alarmType;

    /**
     * 类型名称
     */
    @ExcelProperty("告警类型")
    private String typeName;

    /**
	 * 详情路径
	 */
	private String viewPath;

    /**
     * 告警等级
     */
	@ExcelProperty(value = "告警等级", converter = AlarmLevelConverter.class)
    private Integer alarmLevel;

	/**
     * 告警次数
     */
	@ExcelProperty("告警次数")
    private Integer alarmTimes;

    /**
     * 告警描述
     */
	@ExcelProperty("告警描述")
    private String alarmDesc;

    /**
     * 详细内容
     */
	@ExcelProperty(value = "告警详情", converter = JsonConverter.class)
    private Object alarmContent;

    /**
     * 首次告警时间
     */
	@ExcelProperty(value = "首次告警时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstTime;

    /**
     * 最近告警时间
     */
	@ExcelProperty(value = "最近告警时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;

	/**
     * 告警状态
     */
    @ExcelProperty(value = "告警状态", converter = AlarmStatusConverter.class)
    private Integer alarmStatus;

    /**
     * 处理人
     */
    private Long resolveUser;

    /**
     * 处理时间
     */
	@ExcelProperty(value = "处理时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resolveTime;

    /**
     * 处理方式
     */
	@ExcelProperty(value = "处理方式", converter = AlarmHandlerConverter.class)
    private Integer resolveType;

	/**
	 * 处理意见
	 */
	@ExcelProperty(value = "处理意见")
	private String resolveMsg;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public void recordAlarm(String alarmCode, String sourceName, String alarmType, String desc, Object content){
        this.firstTime = new Date();
        this.lastTime = new Date();
        this.setSourceName(sourceName);
        this.setAlarmType(alarmType);
        this.setAlarmCode(alarmCode);
        this.setAlarmDesc(desc);
        this.setAlarmContent(content);
    }
}
