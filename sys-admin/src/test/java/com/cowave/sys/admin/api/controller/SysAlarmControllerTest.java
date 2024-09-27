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
import com.cowave.sys.admin.api.controller.sys.SysAlarmController;
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
public class SysAlarmControllerTest extends SpringTest {

    @Autowired
    private SysAlarmController sysAlarmController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysAlarmController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 类型列表
     */
    @Test
    public void typeList() throws Exception {
        mockPost("/api/v1/alarm/type/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 类型新增
     */
    @Test
    @Rollback()
    @Transactional
    public void typeAdd() throws Exception {
        mockPost("/api/v1/alarm/type/add", "{\"typeName\":\"测试类型\",\"typeView\":\"/xx/ss\"}");
    }

    /**
     * 类型修改
     */
    @Test
    @Rollback()
    @Transactional
    public void typeEdit() throws Exception {
        mockPost("/api/v1/alarm/type/edit", "{\"id\":1,\"typeName\":\"测试类型\",\"typeView\":\"/xx/ss\"}");
    }

    /**
     * 类型删除
     */
    @Test
    @Rollback()
    @Transactional
    public void typeDelete() throws Exception {
        mockGet("/api/v1/alarm/type/delete?id=1");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/alarm/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/alarm/info?id=1");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/alarm/delete?id=1");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/alarm/export", "{\"page\":1,\"pageSize\":1}", "target/alarm.xlsx");
    }

    /**
     * 告警处理
     */
    @Test
    public void handle() throws Exception {
        mockPost("/api/v1/alarm/handle", "{\"id\":1,\"alarmStatus\":1}");
    }
}
