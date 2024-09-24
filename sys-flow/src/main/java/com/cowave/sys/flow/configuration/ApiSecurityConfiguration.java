/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.flow.configuration;

import com.cowave.commons.framework.filter.security.TokenService;
import org.flowable.engine.IdentityService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Api接口权限过滤器
 *
 * @author shanhuiming
 */
@Configuration
public class ApiSecurityConfiguration {

    /**
     * Api接口访问控制
     */
    @Bean
    public FilterRegistrationBean<ApiSecurityFilter> securityFilterRegistration(
            TokenService tokenService, IdentityService identityService) {
        FilterRegistrationBean<ApiSecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiSecurityFilter(tokenService, identityService));
        registration.setName("sysSecurityFilter");
        registration.addUrlPatterns("/api/v1/*");
        registration.setOrder(100);
        return registration;
    }

    /**
     * 跨域设置
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOriginPatterns("*").allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            }
        };
    }
}
