/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.resource;

import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 转发器
 *
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Transponder extends AccessUser {

    /**
     * 转发器id
     */
    private Integer tspdId;

    /**
     * 转发器名称
     */
    @NotNull(message = "转发器名称不能为空")
    private String tspdName;

    /**
     * 转发器信息版本
     */
    private Long tspdVersion;

    /**
     * 转发器类型
     */
    private Integer tspdType;

    /**
     * 转发器状态
     */
    private Integer tspdStatus;

    /**
     * 卫星id
     */
    @NotNull(message = "卫星id不能为空")
    private Integer satId;

    /**
     * 卫星名称
     */
    private String satName;

    /**
     * 上行波束id
     */
    @NotNull(message = "上行波束id不能为空")
    private Integer upBeamId;

    /**
     * 上行波束名称
     */
    private String upBeamName;

    /**
     * 上行通道id
     */
    @NotNull(message = "上行通道id不能为空")
    private Integer upTunnelId;

    /**
     * 上行通道名称
     */
    private String upTunnelName;

    /**
     * 上行极化方式
     */
    private Integer upPolarization;

    /**
     * 下行波束id
     */
    @NotNull(message = "下行波束id不能为空")
    private Integer downBeamId;

    /**
     * 下行波束名称
     */
    private String downbeamName;

    /**
     * 下行通道id
     */
    @NotNull(message = "下行通道id不能为空")
    private Integer downTunnelId;

    /**
     * 下行通道名称
     */
    private String downTunnelName;

    /**
     * 下行极化方式
     */
    private Integer downPolarization;

    /**
     * 频差
     */
    private Long freqDiff;

    /**
     * 饱和通量密度：dBW/m2
     */
    private BigDecimal sfd;

    /**
     * sfp
     */
    private BigDecimal sfp;

    /**
     * 有效辐射功率：dBW
     */
    private BigDecimal erip;

    /**
     * G/T值：dB/K
     */
    private BigDecimal gt;

    /**
     * 载波输入回退：dB
     */
    private Integer cbi;

    /**
     * 载波输出回退：dB
     */
    private Integer cbo;

    /**
     * gain
     */
    private BigDecimal gain;

    /**
     * 输入功率补偿
     */
    private BigDecimal compensateIn;

    /**
     * 输出功率补偿
     */
    private BigDecimal compensateOut;

    /**
     * 是否同步更新资源
     */
    private boolean syncResource;

    /**
     * 创建人
     */
    private String createUuser;

    /**
     * 创建人名称
     */
    private String createUsername;

    /**
     * 创建单位
     */
    private String createDept;

    /**
     * 创建单位名称
     */
    private String createDeptname;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新人名称
     */
    private String updateUsername;

    /**
     * 更新单位
     */
    private String updateDept;

    /**
     * 更新单位名称
     */
    private String updateDeptname;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
