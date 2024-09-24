/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * @author shanhuiming
 *
 */
@ConfigurationProperties(prefix = "blog")
@Configuration
@Data
public class BlogConfiguration {

    /** 静态资源路径 */
    private String profile;

    /** IP地址获取 */
    private boolean addressEnabled;

    /** 开启页面静态化 */
    private boolean pageStaticEnabled;

    /**
     * 静态页面路径
     */
    public String getHtmlPath() {
        return profile + "/html";
    }

    /**
     * 文章图片路径
     */
    public String getPostImagePath() {
        return profile + "/images/post";
    }

    /**
     * Md图片路径
     */
    public String getMdImagePath() {
        return profile + "/images/markdown";
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public EhCacheCacheManager ehcacheCacheManager() {
        return new EhCacheCacheManager(EhCacheManagerUtils.buildCacheManager("classpath:ehcache.xml"));
    }
}
