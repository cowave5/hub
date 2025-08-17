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
import com.cowave.hub.admin.app.sys.HubAttachController;
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

import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
public class HubAttachControllerTest extends SpringTest {

    @Autowired
    private HubAttachController hubAttachController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(hubAttachController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 上传
     */
    @Test
    public void upload() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("ownerId", "2");
        params.set("ownerModule", SYSTEM_USER);
        params.set("attachType",AVATAR.getVal());
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
    }

    /**
     * 下载
     */
    @Test
    public void download() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("ownerId", "2");
        params.set("ownerModule", SYSTEM_USER);
        params.set("attachType", AVATAR.getVal());
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/attach/download?attachId=1")
                        .header("X-Request-ID", requestId())
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
        params.set("ownerId", "2");
        params.set("ownerModule", SYSTEM_USER);
        params.set("attachType", AVATAR.getVal());
        mockImport("/api/v1/attach/upload", params, "source/cw.jpg");
        mockGet("/api/v1/attach/preview?attachId=1");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/attach/list?ownerId=6&ownerModule=sys-user&attachType=avatar");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/attach/delete?attachId=2");
    }
}
