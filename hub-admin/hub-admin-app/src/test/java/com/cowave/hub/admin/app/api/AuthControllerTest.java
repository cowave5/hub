/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.api;

import com.cowave.commons.framework.access.filter.AccessFilter;
import com.cowave.commons.framework.access.security.BearerTokenFilter;
import com.cowave.hub.admin.app.SpringTest;
import com.cowave.hub.admin.app.auth.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author shanhuiming
 */
public class AuthControllerTest extends SpringTest {

    @Autowired
    private AuthController authController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 获取验证码
     */
    @Test
    public void captcha() throws Exception {
        mockGet("/api/v1/auth/captcha");
    }

    /**
     * 获取邮箱验证码
     */
    @Test
    public void captchaEmail() throws Exception {
        mockGet("/api/v1/auth/captcha/email?email=shanhm1991@163.com");
    }

    /**
     * 注册
     */
    @Test
    public void register() throws Exception {
        redisHelper.putValue("hub-admin:captcha:register", "test@Cowave.com");
        mockPost("/api/v1/auth/register", "{\"userEmail\":\"test@Cowave.com\",\"captcha\":\"register\"," +
                        "\"userName\":\"注册测试\",\"userAccount\":\"testRegister\"}");
    }

    /**
     * 登录
     */
    @Test
    public void login() throws Exception {
        redisHelper.putValue("hub-admin:captcha:login", "21");
        mockPost("/api/v1/auth/login", "{\"userAccount\":\"liubei\",\"passWord\":\"12345678\",\"captchaId\":\"login\",\"captcha\":\"21\"}");
    }

    /**
     * 登录
     */
    @Test
    public void logon() throws Exception {
        mockPost("/api/v1/auth/logon", "{\"userAccount\":\"liubei\",\"passWord\":\"12345678\"}");
    }

    /**
     * 退出
     */
    @Test
    public void logout() throws Exception {
        mockGet("/api/v1/auth/logout");
    }

    /**
     * 令牌刷新
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/auth/refresh?refreshToken=" + refreshToken);
    }

    /**
     * 登录信息
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/auth/info");
    }

    /**
     * 路由信息
     */
    @Test
    public void routes() throws Exception {
        mockGet("/api/v1/auth/routes");
    }

    /**
     * 在线用户
     */
    @Test
    public void onlineList() throws Exception {
        mockPost("/api/v1/auth/online", "{}");
    }

    /**
     * 强退用户
     */
    @Test
    public void outline() throws Exception {
        mockGet("/api/v1/auth/outline/user/zhangfei");
    }
}
