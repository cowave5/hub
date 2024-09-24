/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import lombok.Data;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class RouteMeta {

    /**
     * 设置路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置路由图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    public RouteMeta()
    {
    }

    public RouteMeta(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public RouteMeta(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public RouteMeta(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public RouteMeta(String title, String icon, boolean noCache, String link)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.link = link;
    }

}
