/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.resource;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.cowave.commons.framework.access.AccessUser;
import com.cowave.commons.tools.Asserts;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资源池
 *
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ResourcePool extends AccessUser {

    /**
     * 主键
     **/
    private Long id;

    /**
     * 资源id
     **/
    private Long srcId;

    /**
     * 资源状态
     **/
    private Integer srcStatus;

    /**
     * 资源类型
     **/
    private Integer srcType;

    /**
     * 卫星id
     **/
    private Integer satId;

    /**
     * 卫星名称
     **/
    private String satName;

    /**
     * 下行波束id
     **/
    private Integer downBeamId;

    /**
     * 下行波束名称
     **/
    private String downBeamName;

    /**
     * 下行通道id
     **/
    private Integer downTunnelId;

    /**
     * 下行通道名称
     **/
    private String downTunnelName;

    /**
     * 转发器id
     **/
    private Integer tspdId;

    /**
     * 转发器名称
     **/
    private String tspdName;

    /**
     * 上行波束id
     **/
    private Integer upBeamId;

    /**
     * 上行波束名称
     **/
    private String upBeamName;

    /**
     * 上行通道id
     **/
    private Integer upTunnelId;

    /**
     * 上行通道名称
     **/
    private String upTunnelName;

    /**
     * 卫星信息版本
     */
    private Long satVersion;

    /**
     * 转发器信息版本
     */
    private Long tspdVersion;

    /**
     * 上行波束信息版本
     */
    private Long upBeamVersion;

    /**
     * 上行通道信息版本
     */
    private Long upTunnelVersion;

    /**
     * 下行波束信息版本
     */
    private Long downBeamVersion;

    /**
     * 下行通道信息版本
     */
    private Long downTunnelVersion;

    /**
     * 起始时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeBegin;

    /**
     * 结束时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    /**
     * 时间宽度
     **/
    private Long timewidth;

    /**
     * 下行起始频点
     **/
    private Long downFreqBegin;

    /**
     * 下行终止频点
     **/
    private Long downFreqEnd;

    /**
     * 带宽
     **/
    private Long bandwidth;

    /**
     * 频差
     **/
    private Long freqDiff;

    /**
     * 上行起始频点
     **/
    private Long upFreqBegin;

    /**
     * 上行终止频点
     **/
    private Long upFreqEnd;

    /**
     * 最小使用单位(子带)
     **/
    private Long usageUnit;

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
     * 转发器类型
     */
    private Integer tspdType;

    /**
     * 下行波束类型
     **/
    private Integer downBeamType;

    /**
     * 下行波束覆盖范围
     */
    private String downBeamCoverage;

    /**
     * 下行通道类型
     **/
    private Integer downTunnelType;

    /**
     * 下行频段类型
     **/
    private Integer downFreqType;

    /**
     * 下行极化方式
     **/
    private Integer downPolarization;

    /**
     * 上行波束类型
     **/
    private Integer upBeamType;

    /**
     * 上行波束覆盖范围
     */
    private String upBeamCoverage;

    /**
     * 上行通道类型
     **/
    private Integer upTunnelType;

    /**
     * 上行频段类型
     **/
    private Integer upFreqType;

    /**
     * 上行极化方式
     **/
    private Integer upPolarization;

    /**
     * 占用单位
     */
    private Long occupyDept;

    /**
     * 占用单位名称
     */
    private Long occupyDeptname;

    /**
     * 归属单位
     */
    private String ownerDept;

    /**
     * 归属单位名称
     */
    private String ownerDeptname;

    /**
     * 来源单位(A->B->C A为归属 B为来源)
     **/
    private String fromDept;

    /**
     * 来源单位
     **/
    private String formDeptname;

    /**
     * 来源时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date formTime;

    /**
     * 来源记录
     **/
    private Long formUsage;

    /**
     * 来源流程
     **/
    private String formFlow;

    /**
     * 来源任务
     **/
    private String formTask;

    /**
     * 来源事务
     **/
    private String formDuty;

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

    public Long getTimewidth() {
        if(timewidth == null && timeBegin != null && timeEnd != null) {
            return DateUtil.between(timeBegin, timeEnd, DateUnit.SECOND);
        }
        return timewidth;
    }

    public Long getBandwidth() {
        if(bandwidth == null && downFreqBegin != null && downFreqEnd != null) {
            return downFreqEnd - downFreqBegin;
        }
        return bandwidth;
    }

    public void validGenerate(){
        Asserts.notNull(tspdId, "转发器id为空");
        Asserts.notNull(timeBegin, "开始时间不能为空");
        Asserts.notNull(timeEnd, "结束时间不能为空");
    }
}
