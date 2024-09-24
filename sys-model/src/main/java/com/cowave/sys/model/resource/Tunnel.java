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

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * 通道
 *
 * @author shanhuiming
 */
@Data
public class Tunnel extends AccessUser {

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
    @NotNull(message = "波束id不能为空")
    private Integer beamId;

    /**
     * 波束名称
     */
    private String beamName;

    /**
     * 通道id
     */
    private Integer tunnelId;

    /**
     * 通道名称
     */
    @NotNull(message = "通道名称不能为空")
    private String tunnelName;

    /**
     * 通道信息版本
     */
    private Long tunnelVersion;

    /**
     * 通道类型
     */
    private Integer tunnelType;

    /**
     * 频段类型
     */
    private Integer freqType;

    /**
     * 起始频点【单位:Hz】
     */
    private Long freqBegin;

    /**
     * 终止频点【单位:Hz】
     */
    private Long freqEnd;

    /**
     * 带宽【单位:Hz】
     */
    private Long bandwidth;

    /**
     * 最小使用带宽【单位:Hz】
     */
    private Long usageUnit;

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

    public Long getBandwidth() {
        if(bandwidth == null && freqBegin != null && freqEnd != null) {
            return freqEnd - freqBegin;
        }
        return bandwidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tunnel tunnel)) {
            return false;
        }
        return Objects.equals(tunnelId, tunnel.tunnelId);
    }

    @Override
    public int hashCode() {
        return tunnelId;
    }
}
