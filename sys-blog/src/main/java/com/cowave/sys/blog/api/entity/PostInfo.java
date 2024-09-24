/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章
 *
 * @author shanhuiming
 */
@Data
public class PostInfo {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 封面
     */
    private String image;

    /**
     * 文章密码
     */
    private String pwd;

    /**
     * 是否作为幻灯片
     */
    private Integer slider;

    /**
     * 是否可见
     */
    private Integer status;

    /**
     * 是否推荐
     */
    private Integer featured;

    /**
     * 原创/转载
     */
    private Integer original;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer favors;

    /**
     * 评论数
     */
    @TableField(exist = false)
    private Integer comments;

    /**
     * 专栏id
     */
    private Long channelId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 创建人
     */
    private String createUser;

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
     * 标签
     */
    @TableField(exist = false)
    private String tags;

    /**
     * 文章内容
     */
    @TableField(exist = false)
    private String content;

    /**
     * 专栏目录 侧边栏显示
     */
    @TableField(exist = false)
    private List<PostInfo> specialPostList;

    /**
     * 专栏名称
     */
    @TableField(exist = false)
    private String channelName;

    /**
     * 专栏名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 作者
     */
    @TableField(exist = false)
    private String username;

    /**
     * 作者头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 栏目类型：1分类，2专题
     */
    @TableField(exist = false)
    private String channelType;

    /**
     * 编辑器 markdown/tinymce
     */
    @TableField(exist = false)
    private String editor = "markdown";
}
