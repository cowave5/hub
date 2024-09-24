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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 卫星
 *
 * @author shanhuiming
 */
@Data
public class Satellite extends AccessUser {

    /**
     * 卫星id
     */
    private Integer satId;

    /**
     * 卫星名称
     */
    @NotNull(message = "卫星名称不能为空")
    private String satName;

    /**
     * 卫星信息版本
     */
    private Long satVersion;

    /**
     * 卫星类型
     */
    private Integer satType;

    /**
     * 卫星轨道
     */
    private Integer satOrbit;

    /**
     * 卫星平台
     */
    private Integer satPlat;

    /**
     * 信标频率
     */
    private Integer beacon;

    /**
     * 频段类型
     */
    private Integer freqType;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 管理单位
     */
    private String maintainer;

    /**
     * 运营单位
     */
    private String operator;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 轨道高度(单位:公里)
     */
    @Max(value = 36000, message = "轨道高度不能大于36000公里")
    @Min(value = 200, message = "轨道高度不能小于200公里")
    private BigDecimal altitude;

    /**
     * 发射时间：yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date launchDate;

    /**
     * 定轨时间：yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orbitDate;

    /**
     * 使用年限
     */
    private Integer serviceYears;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Satellite satellite)) {
            return false;
        }
        return Objects.equals(satId, satellite.satId);
    }

    @Override
    public int hashCode() {
        return satId;
    }
}
