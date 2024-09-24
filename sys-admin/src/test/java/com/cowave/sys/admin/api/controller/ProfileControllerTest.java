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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author shanhuiming
 *
 */
public class ProfileControllerTest extends SpringTest {

    @Autowired
    private ProfileController profileController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
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
        params.set("masterId", "2");
        params.set("attachGroup", "sys-user");
        params.set("attachType", "avatar");
        mockImport("/api/v1/profile/avatar", params, "source/cw.jpg");
    }
}
