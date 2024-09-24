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
import java.util.List;

import javax.validation.constraints.NotNull;

import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公告
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysNotice extends AccessUser {

    /** 草稿 **/
    public static final int STATUS_DRAFT = 0;

    /** 已发布 **/
    public static final int STATUS_PUBLISH = 1;

	/** 已撤回 **/
	public static final int STATUS_RECALL = 2;

	/**
	 * 公告id
	 */
	private Long noticeId;

	/**
	 * 公告标题
	 */
	@NotNull(message = "notice.notnul.title")
	private String noticeTitle;

	/**
	 * 公告状态 0草稿 1已发布 2已撤回
	 */
    private Integer noticeStatus;

	/**
	 * 公告类型 0 公告 1 通知
	 */
	private Integer noticeType;

	/**
	 * 公告等级 0 普通 1 紧急
	 */
	private Integer noticeLevel;

	/**
	 * 公告内容
	 */
	private String content;

	/**
	 * 是否系统公告 0 否 1 是
	 */
	private Integer isSystem;

	/**
	 * 总人数
	 */
	private Integer statTotal;

	/**
     * 已读人数
     */
    private Integer statRead;

    /**
     * 目标是否全员  0 否 1 是
     */
    private Integer goalsAll;

    /**
     * 目标单位
     */
    private List<Long> goalsDept;

    /**
     * 目标角色
     */
    private List<Long> goalsRole;

    /**
     * 目标用户
     */
    private List<Long> goalsUser;

	/**
	 * 发布时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishTime;

	/**
	 * 图片附件
	 */
	private List<SysAttach> attachs;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建部门
	 */
	private Long createDept;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新部门
	 */
	private Long updateDept;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 创建人名称
	 */
	private String createUserName;

    /**
     * 已读状态
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
}
