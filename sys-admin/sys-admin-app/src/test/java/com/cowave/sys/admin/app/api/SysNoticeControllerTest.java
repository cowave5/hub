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
import com.cowave.sys.admin.app.base.SysNoticeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author shanhuiming
 */
public class SysNoticeControllerTest extends SpringTest {

    @Autowired
    private SysNoticeController sysNoticeController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysNoticeController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/notice/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/notice/info/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/notice/add", "{\"noticeTitle\":\"测试公告\",\"content\":\"今晚聚餐\",\"attachs\":[]}");
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/notice/edit", "{\"noticeId\":1,\"noticeTitle\":\"测试公告\",\"content\":\"今晚聚餐\",\"attachs\":[]}");
    }

    /**
     * 图片上传
     */
    @Test
    public void imageUpload() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("attachType", "test");
        mockImport("/api/v1/notice/image", params, "source/cw.jpg");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/notice/delete?noticeId=1");
    }

    /**
     * 发布
     */
    @Test
    @Rollback()
    @Transactional
    public void publish() throws Exception {
        mockGet("/api/v1/notice/publish/1");
    }

    /**
     * 已读列表
     */
    @Test
    public void readList() throws Exception {
        mockGet("/api/v1/notice/read/list?noticeId=1");
    }

    /**
     * 消息列表
     */
    @Test
    public void msgList() throws Exception {
        mockGet("/api/v1/notice/msg/list");
    }

    /**
     * 阅读消息
     */
    @Test
    @Rollback()
    @Transactional
    public void msgRead() throws Exception {
        mockGet("/api/v1/notice/msg/read/1");
    }

    /**
     * 反馈消息
     */
    @Test
    @Rollback()
    @Transactional
    public void msgBack() throws Exception {
        mockGet("/api/v1/notice/msg/back?noticeId=1&readBack=测试反馈");
    }

    /**
     * 删除消息
     */
    @Test
    @Rollback()
    @Transactional
    public void msgDelete() throws Exception {
        mockGet("/api/v1/notice/msg/delete?noticeId=1");
    }
}
