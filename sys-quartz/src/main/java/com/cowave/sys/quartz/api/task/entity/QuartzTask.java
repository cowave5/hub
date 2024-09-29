/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.support.excel.DateConverter;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.quartz.api.task.job.ParallelJob;
import com.cowave.sys.quartz.api.task.job.SerialJob;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
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
@EqualsAndHashCode(callSuper=false)
public class QuartzTask extends AccessInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5823564567511005149L;

    public static final String JOB_PROPERTIES = "JOB_PROPERTIES";

    /**
     * 任务id
     */
    private Long id;

    /**
     * 任务名称
     */
    @ExcelProperty("任务名称")
    @NotBlank(message = "任务名称不能为空")
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
    @NotBlank(message = "调用目标不能为空")
    private String invokeTarget;

    /**
     * cron表达式
     */
    @ExcelProperty("cron表达式")
    @NotBlank(message = "cron表达式不能为空")
    private String cron;

    /**
     * 1启动 0不启动
     */
    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    private int status;

    /**
     * 计划策略 0 1 2 -1
     */
    @ExcelProperty("计划策略")
    private int policy;

    /**
     * 任务优先级
     */
    @ExcelProperty("优先级")
    private int priority;

    /**
     * 是否并发执行 0否 1是
     */
    @ExcelProperty(value = "是否并发", converter = YesNoConverter.class)
    private int concurrent;

    /**
     * 执行次数
     */
    @ExcelProperty("执行次数")
    private int times;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间", converter = DateConverter.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间", converter = DateConverter.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新部门
     */
    private Integer updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * WAITING 等待 PAUSED 暂停 ACQUIRED 正常执行 BLOCKED 阻塞 ERROR 错误
     */
    private String taskStatus;

    /**
     * 上次执行时间
     */
    private Long timePrev;

    /**
     * 下次执行时间
     */
    private Long timeNext;

    public Class<? extends Job> ofJobClass() {
        return 1 == concurrent ? ParallelJob.class : SerialJob.class;
    }

    public CronScheduleBuilder ofCronScheduleBuilder() {
        Asserts.isTrue(CronExpression.isValidExpression(cron), "非法cron表达式");
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        return switch (policy) {
            // 智能的策略，针对不同的Trigger执行不同，CronTrigger时为MISFIRE_INSTRUCTION_FIRE_ONCE_NOW
            case 0 -> cronScheduleBuilder;
            // 立即执行一次，然后按照Cron定义时间点执行
            case 1 -> cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            // 什么都不做，等待Cron定义下次任务执行的时间点
            case 2 -> cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            // 将所有错过的执行时间点全都补上
            case -1 -> cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            default -> cronScheduleBuilder;
        };
    }

    public String getTimePrev(){
        if(timePrev == null || timePrev == -1){
            return null;
        }
        return DateUtils.format(new Date(timePrev));
    }

    public String getTimeNext(){
        if(timeNext == null || timeNext == 0){
            return null;
        }
        return DateUtils.format(new Date(timeNext));
    }
}
