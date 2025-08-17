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
import com.cowave.hub.admin.app.auth.OAuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class OAuthControllerTest extends SpringTest {

    @Autowired
    private OAuthController oAuthController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(oAuthController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void saveConfig() throws Exception {
        mockPost("/api/v1/oauth/config/save", "{\"serverType\":\"gitlab\",\"appId\":\"asadefewfef\"}");
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
        mockPost("/api/v1/oauth/user/list", "{\"serverType\":\"gitlab\"}");
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
