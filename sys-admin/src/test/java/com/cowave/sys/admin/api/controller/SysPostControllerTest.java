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
import com.cowave.sys.admin.api.controller.sys.SysPostController;
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
public class SysPostControllerTest extends SpringTest {

    @Autowired
    private SysPostController sysPostController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysPostController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 刷新缓存
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/post/refresh");
    }

    /**
     * 岗位关系
     */
    @Test
    public void tree() throws Exception {
        mockGet("/api/v1/post/tree");
    }

    /**
     * 部门岗位树
     */
    @Test
    public void deptPostTree() throws Exception {
        mockGet("/api/v1/post/tree/dept");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/post/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/post/info/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/post/add", "{\"postName\":\"测试岗位\"}");
    }

    /**
     * 编辑
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/post/edit", "{\"postId\":3,\"postName\":\"测试岗位\"}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/post/delete?postId=21,22,23");
    }

    /**
     * 修改只读
     */
    @Test
    @Rollback()
    @Transactional
    public void changeReadonly() throws Exception {
        mockPost("/api/v1/post/change/readonly", "{\"postId\":3,\"readOnly\":1}");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/post/export", "{\"page\":1,\"pageSize\":1}", "target/post.xlsx");
    }
}
