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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 使用记录
 *
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ResourceUsage extends AccessUser {

    /**
     * 主键
     **/
    private Long id;

    /**
     * 使用id
     **/
    private Long usageId;

    /**
     * 使用状态
     **/
    private Integer usageStatus;

    /**
     * 使用类型
     **/
    private Integer usageType;

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
     * 终止时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    /**
     * 时宽
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
     * 资源id
     **/
    private Long srcId;

    /**
     * 资源类型
     **/
    private Integer srcType;

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
    private String ownerDeptName;

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
     * 合并记录id
     **/
    private long mergeId = -1;

    /**
     * 调整原始id
     **/
    private long originId = -1;

    /**
     * 调整前id
     **/
    private long prevId = -1;

    /**
     * 调整前usageId
     **/
    private long prevUsageId = -1;

    /**
     * 调整次数
     **/
    private int prevTimes = 0;

    /**
     * 上一步动作
     **/
    private int prevOption = -1;

    /**
     * 上一步状态
     **/
    private int prevStatus = -1;

    /**
     * 使用单位
     **/
    private String usageDept;

    /**
     * 使用单位名称
     **/
    private String usageDeptname;

    /**
     * 任务id
     **/
    private String taskId;

    /**
     * 任务名称
     **/
    private String taskName;

    /**
     * 网系标识
     **/
    private int systemId = -1;

    /**
     * 子网标识
     **/
    private String netNo;

    /**
     * 组网模式
     **/
    private int netMode = -1;

    /**
     * 事务id
     **/
    private String dutyId;

    /**
     * 流程id
     **/
    private String flowId;

    /**
     * 预案id
     **/
    private long planId = -1;

    /**
     * 创建人
     **/
    private String createUser;

    /**
     * 创建人
     **/
    private String createUserName;

    /**
     * 创建部门
     **/
    private String createDept;

    /**
     * 创建部门
     **/
    private String createDeptName;

    /**
     * 创建时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     **/
    private String updateUser;

    /**
     * 更新人
     **/
    private String updateUserName;

    /**
     * 更新部门
     **/
    private String updateDept;

    /**
     * 更新部门
     **/
    private String updateDeptName;

    /**
     * 更新时间
     **/
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
}
