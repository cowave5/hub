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
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 波束
 *
 * @author shanhuiming
 */
@Data
public class Beam extends AccessUser {

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
     * 波束id
     */
    private Integer beamId;

    /**
     * 波束名称
     */
    @NotNull(message = "波束名称不能为空")
    private String beamName;

    /**
     * 波束信息版本
     */
    private Long beamVersion;

    /**
     * 波束类型
     */
    private Integer beamType;

    /**
     * 波束天线
     */
    private Integer beamAntenna;

    /**
     * 频段类型
     */
    private Integer freqType;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 半径
     */
    private BigDecimal radius;

    /**
     * EIRP
     */
    private BigDecimal eirp;

    /**
     * GT
     */
    private BigDecimal gt;

    /**
     * 夹角边缘衰减
     */
    private BigDecimal angleEdge;

    /**
     * 波束夹角(3db)
     */
    private Integer angle;

    /**
     * 波束范围
     */
    private String coverage;

    /**
     * 覆盖面积(描述性)
     */
    private String coverageArea;

    /**
     * 移动标识
     */
    private Integer moveFlag;

    /**
     * 工作模式
     */
    private Integer workMode;

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
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beam beam)) {
            return false;
        }
        return Objects.equals(beamId, beam.beamId);
    }

    @Override
    public int hashCode() {
        return beamId;
    }
}
