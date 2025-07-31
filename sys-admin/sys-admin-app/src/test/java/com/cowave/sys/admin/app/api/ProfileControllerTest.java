/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.api;

import com.cowave.commons.framework.access.filter.AccessFilter;
import com.cowave.commons.framework.access.security.BearerTokenFilter;
import com.cowave.sys.admin.app.SpringTest;
import com.cowave.sys.admin.app.auth.ProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.cowave.sys.admin.domain.constants.AttachType.AVATAR;
import static com.cowave.sys.admin.domain.constants.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
public class ProfileControllerTest extends SpringTest {

    @Autowired
    private ProfileController profileController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/profile/info");
    }

    /**
     * 编辑
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/profile/edit", "{\"userId\":2,\"userName\":\"测试人员\",\"userEmail\":\"test@Cowave.com\"}");
    }

    /**
     * 重置密码
     */
    @Test
    @Rollback()
    @Transactional
    public void resetPasswd() throws Exception {
        mockPost("/api/v1/profile/passwd/reset", "{\"userId\":2,\"oldPasswd\":\"12345678\",\"newPasswd\":\"123456\"}");
    }

    /**
     * 头像上传
     */
    @Test
    public void imageUpload() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("ownerId", "2");
        params.set("ownerModule", SYSTEM_USER);
        params.set("attachType", AVATAR);
        mockImport("/api/v1/profile/avatar", params, "source/cw.jpg");
    }
}
