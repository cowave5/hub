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
import com.cowave.sys.admin.api.controller.sys.SysAttachController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author shanhuiming
 *
 */
public class SysAttachControllerTest extends SpringTest {

    @Autowired
    private SysAttachController sysAttachController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysAttachController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 上传
     */
    @Test
    public void upload() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("masterId", "2");
        params.set("attachGroup", "sys-user");
        params.set("attachType", "avatar");
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
    }

    /**
     * 下载
     */
    @Test
    public void download() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("masterId", "2");
        params.set("attachGroup", "sys-user");
        params.set("attachType", "avatar");
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/attach/download?attachId=1")
                        .header("requestId", requestId())
                        .header("Authorization", accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 预览
     */
    @Test
    public void preview() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("masterId", "2");
        params.set("attachGroup", "sys-user");
        params.set("attachType", "avatar");
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
        mockGet("/api/v1/attach/preview?attachId=1");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/attach/list?masterId=6&attachGroup=sys-user&attachType=avatar");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/attach/delete?attachId=2");
    }
}
