/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 动态
 *
 * @author shanhuiming
 */
@Data
@TableName("info_note")
public class NoteInfo {

    /**
     * 唯一id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String noteTitle;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 图片（预留字段，暂时不考虑）
     */
    private String noteImage;

    /**
     * 内容
     */
    private String noteContent;

    /**
     * 摘要
     */
    private String noteSummary;

    /**
     * 类型（数据字典，不写死）
     */
    private String noteType;

    /**
     * 状态
     */
    private String noteStatus;

    /**
     * 是否公共，0公共，  1 私有
     */
    private String isPublic;

    /**
     * 创建时间
     */
     @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
     @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String praiseNum;
}
