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
import com.cowave.sys.admin.api.controller.sys.SysNoticeController;
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
public class SysNoticeControllerTest extends SpringTest {

    @Autowired
    private SysNoticeController sysNoticeController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysNoticeController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
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
