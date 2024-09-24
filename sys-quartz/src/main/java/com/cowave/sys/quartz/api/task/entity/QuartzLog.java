/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.commons.framework.support.excel.DateConverter;
import com.cowave.commons.framework.support.excel.SuccessConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class QuartzLog {

    /**
     * id
     */
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    @ExcelProperty("任务名称")
    private String taskName;

    /**
     * 任务分组
     */
    @ExcelProperty("任务分组")
    private String taskGroup;

    /**
     * 调用目标
     */
    @ColumnWidth(60)
    @ExcelProperty("调用目标")
    private String invokeTarget;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间", converter = DateConverter.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "开始时间", converter = DateConverter.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 耗时
     */
    @ExcelProperty(value = "耗时[ms]")
    private Long costTime;

    /**
     * 返回结果
     */
    private Object content;

    /**
     * 结果 1成功 0失败
     */
    @ExcelProperty(value = "执行结果", converter = SuccessConverter.class)
    private Integer status;

    /**
     * 异常信息
     */
    @ColumnWidth(100)
    @ExcelProperty(value = "异常信息")
    private String throwable;
}
