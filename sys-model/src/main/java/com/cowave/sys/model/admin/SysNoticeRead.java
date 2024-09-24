/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公告已读
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysNoticeRead extends AccessUser {

    /** 消息未读 **/
    public static final int STATUS_PUBLISH_UNREAD = 0;

    /** 消息未读删除 **/
    public static final int STATUS_DELETE_UNREAD = 1;

    /** 消息未读撤回 **/
    public static final int STATUS_RECALL_UNREAD = 2;

    /** 消息已读 **/
    public static final int STATUS_PUBLISH_READ = 10;

    /** 消息已读删除 **/
    public static final int STATUS_DELETE_READ = 11;

    /** 消息已读撤回 **/
    public static final int STATUS_RECALL_READ = 22;

    /**
     * id
     */
    private Long id;

    /**
     * 公告id
     */
    @NotNull(message = "notice.notnull.id")
    private Long noticeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 已读状态 0 否 1 是 2 read删除 3 notice删除
     */
    private Integer readStatus;

    /**
     * 已读反馈
     */
    private String readBack;

    /**
     * 已读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /**
     * 用户名称
     */
    private String userName;
}
