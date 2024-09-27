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
import com.cowave.sys.admin.api.controller.sys.SysLogController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author shanhuiming
 *
 */
public class SysLogControllerTest  extends SpringTest {

    @Autowired
    private SysLogController sysLogController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysLogController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/log/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/log/info/1");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/log/delete?id=1");
    }

    /**
     * 清空
     */
    @Test
    public void clean() throws Exception {
        mockGet("/api/v1/log/clean");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/log/export", "{\"page\":1,\"pageSize\":1}", "target/log.xlsx");
    }

    /**
     * 用户日志信息查询
     */
    @Test
    public void userLogQuery() throws Exception {
        mockPost("/api/v1/log/user/query", "{\"roleIds\":[1],\"parentIds\":[2],\"deptPostIds\":[\"2-1\"]}");
    }

    /**
     * 部门日志信息查询
     */
    @Test
    public void deptLogQuery() throws Exception {
        mockPost("/api/v1/log/dept/query", "{\"parentIds\":[2]}");
    }

    /**
     * 岗位日志信息查询
     */
    @Test
    public void postLogQuery() throws Exception {
        mockPost("/api/v1/log/post/query", "{\"parentIds\":[2]}");
    }
}
