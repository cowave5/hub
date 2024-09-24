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
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 导航栏
 *
 * @author shanhuiming
 */
@Data
@TableName("info_navigation")
public class NavigationInfo {

    /**
     * id
     */
    private Long id;

    /**
     * 父级id
     */
    private Long pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private Integer type;

    /**
     * 页面地址
     */
    private String pageUrl;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 图标
     */
    private String icon;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 父级栏目
     */
    @TableField(exist = false)
    private NavigationInfo parent;

    /**
     * 子栏目
     */
    @TableField(exist = false)
    private List<NavigationInfo> children;
}
