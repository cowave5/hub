/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.commons.framework.filter.access.AccessFilter;
import com.cowave.commons.framework.filter.security.TokenAuthenticationFilter;
import com.cowave.sys.admin.SpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shanhuiming
 *
 */
public class OAuthControllerTest extends SpringTest {

    @Autowired
    private OAuthController oAuthController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(oAuthController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void saveConfig() throws Exception {
        mockPost("/api/v1/oauth/config/save", "{\"appType\":\"gitlab\",\"appId\":\"asadefewfef\"}");
    }

    /**
     * 详情
     */
    @Test
    public void getConfig() throws Exception {
        mockGet("/api/v1/oauth/config/get/gitlab");
    }

    /**
     * 获取授权用户
     */
    @Test
    public void userList() throws Exception {
        mockPost("/api/v1/oauth/user/list", "{\"appType\":\"gitlab\"}");
    }

    /**
     * 修改用户角色
     */
    @Test
    public void changeUserRole() throws Exception {
        mockGet("/api/v1/oauth/user/role/change?userId=1&roleId=1");
    }

    /**
     * 删除用户
     */
    @Test
    public void deleteUser() throws Exception {
        mockGet("/api/v1/oauth/user/delete?userId=1");
    }
}
