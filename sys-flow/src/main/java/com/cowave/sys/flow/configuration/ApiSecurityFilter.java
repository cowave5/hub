/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.flow.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.IdentityService;
import org.springframework.feign.codec.HttpCode;
import org.springframework.feign.codec.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;

/**
 * Api接口权限过滤器
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
public class ApiSecurityFilter implements Filter {

    private final TokenService tokenService;

    private final IdentityService identityService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        AccessToken accessToken = tokenService.parseToken(httpRequest);
        if(StringUtils.isNotBlank(accessToken.getValidCode())) {
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.getWriter().write(JSON.toJSONString(Response.code(new HttpCode() {
                @Override
                public String getCode() {
                    return accessToken.getValidCode();
                }

                @Override
                public String getMsg() {
                    return accessToken.getValidDesc();
                }
            })));
            return;
        }
        identityService.setAuthenticatedUserId(accessToken.getUsername());
        chain.doFilter(request, response);
    }
}
