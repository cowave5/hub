/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 会议流程
 *
 * @author shanhuiming
 */
@Data
public class Meeting {

    /**
     * 待签到
     */
    public static final Integer PROCESS_STATUS_UNSIGN = 1;

    /**
     * 会议中
     */
    public static final Integer PROCESS_STATUS_SIGN = 2;

    /**
     * 已结束
     */
    public static final Integer PROCESS_STATUS_FINISHED = 3;

    /**
     * id
     */
    private Long id;

    /**
     * 会议主题
     */
    private String meetingTopic;

    /**
     * 会议室
     */
    private String meetingRoom;

    /**
     * 参与人员
     */
    private List<Long> members;

    /**
     * 会议纪要
     */
    private String content;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 发起人
     */
    private Long applyUser;

    /**
     * 发起人
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程状态
     */
    private Integer processStatus;
}
