/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.entity.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author shanhuiming
 */
@Data
public class Usage implements Cloneable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 调整动作
     */
    private int option;

    /**
     * 资源id
     */
    private Long srcId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 子网id
     */
    private String netNo;

    /**
     * 转发器id
     */
    @NotNull(message = "tspdId不能为空")
    private Integer tspdId;

    /**
     * 起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "起始时间不能为空")
    private Date timeBegin;

    /**
     * 终止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "终止时间不能为空")
    private Date timeEnd;

    /**
     * 起始频点
     */
    @NotNull(message = "起始频点不能为空")
    private Long freqBegin;

    /**
     * 终止频点
     */
    @NotNull(message = "终止频点不能为空")
    private Long freqEnd;

    @Override
    public Usage clone() {
        try {
            return (Usage) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }
}
