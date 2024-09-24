/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Route {

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏路由
     */
    private boolean hidden;

    /**
     * 重定向地址，设置noRedirect时该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 当一个路由下面的children大于1时，自动会变成嵌套的模式
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private RouteMeta meta;

    /**
     * 子路由
     */
    private List<Route> children;
}
