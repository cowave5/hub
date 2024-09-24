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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 附件
 *
 * @author shanhuiming
 */
@Data
public class SysAttach {

    /**
     * 附件id
     */
    private Long attachId;

    /**
     * 宿主id
     */
    private Long masterId;

    /**
     * 附件分组
     */
    private String attachGroup;

    /**
     * 附件类型
     */
    private String attachType;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件大小
     */
    private Long attachSize;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 预览地址
     */
    private String viewUrl;

    /**
     * 附件状态 0未生效 1已生效
     */
    private Integer attachStatus = 1;

    /**
     * 是否公开的 1是 0否
     */
    private Integer isPublic = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
}
