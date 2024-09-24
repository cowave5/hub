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
import com.cowave.sys.admin.api.controller.sys.SysDeptController;
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
public class SysDeptControllerTest extends SpringTest {

    @Autowired
    private SysDeptController sysDeptController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysDeptController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 刷新缓存
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/dept/refresh");
    }

    /**
     * 树
     */
    @Test
    public void tree() throws Exception {
        mockGet("/api/v1/dept/tree?deptId=3");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/dept/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/dept/info/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/dept/add", "{\"deptName\":\"测试部门\",\"parentIds\":[4]}");
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/dept/edit", "{\"deptId\":8,\"deptName\":\"测试部门\",\"parentIds\":[4]}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/dept/delete?deptId=4,5");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/dept/export", "{\"page\":1,\"pageSize\":1}", "target/dept.xlsx");
    }

    /**
     * 修改只读
     */
    @Test
    @Rollback()
    @Transactional
    public void changeReadonly() throws Exception {
        mockPost("/api/v1/dept/change/readonly", "{\"deptId\":5,\"readOnly\":1}");
    }

    /**
     * 获取部门岗位
     */
    @Test
    public void getPosts() throws Exception {
        mockGet("/api/v1/dept/posts/id/6");
    }

    /**
     * 设置部门岗位
     */
    @Test
    @Rollback()
    @Transactional
    public void setPosts() throws Exception {
        mockPost("/api/v1/dept/posts/set", "[{\"deptId\":4,\"postId\":3,\"isDefault\":1},{\"deptId\":4,\"postId\":4,\"isDefault\":0}]");
    }

    /**
     * 获取部门人员
     */
    @Test
    public void getUsers() throws Exception {
        mockGet("/api/v1/dept/users/id/6");
    }

    /**
     * 设置部门人员
     */
    @Test
    @Rollback()
    @Transactional
    public void setUsers() throws Exception {
        mockPost("/api/v1/dept/users/set", "[{\"deptId\":4,\"userId\":3,\"postId\":3,\"isDefault\":1,\"isLeader\":1},{\"deptId\":4,\"userId\":4,\"postId\":4,\"isDefault\":0,\"isLeader\":0}]");
    }
}
