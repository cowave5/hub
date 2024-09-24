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
import com.cowave.sys.admin.api.controller.sys.SysUserController;
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
public class SysUserControllerTest extends SpringTest {

    @Autowired
    private SysUserController sysUserController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysUserController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 刷新缓存
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/user/refresh");
    }

    /**
     * 人员关系
     */
    @Test
    public void tree() throws Exception {
        mockGet("/api/v1/user/tree");
    }

    /**
     * 部门用户树
     */
    @Test
    public void deptUserTree() throws Exception {
        mockGet("/api/v1/user/tree/dept");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/user/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 列表
     */
    @Test
    public void listWithDept() throws Exception {
        mockPost("/api/v1/user/list", "{\"page\":1,\"pageSize\":1,\"deptId\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/user/info/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/user/add", "{\"userAccount\":\"test\",\"userName\":\"测试人员\",\"userPasswd\":\"123456\"}");
    }

    /**
     * 编辑
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/user/edit", "{\"userId\":5,\"userAccount\":\"test\",\"userName\":\"测试人员\",\"userPasswd\":\"123456\"}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/user/delete?userId=3,4");
    }

    /**
     * 修改状态
     */
    @Test
    @Rollback()
    @Transactional
    public void changeStatus() throws Exception {
        mockPost("/api/v1/user/change/status", "{\"userId\":5,\"userStatus\":0}");
    }

    /**
     * 修改密码
     */
    @Test
    @Rollback()
    @Transactional
    public void changePasswd() throws Exception {
        mockPost("/api/v1/user/change/passwd", "{\"userId\":5,\"userPasswd\":\"123123\"}");
    }

    /**
     * 修改角色
     */
    @Test
    @Rollback()
    @Transactional
    public void changeRoles() throws Exception {
        mockPost("/api/v1/user/change/roles", "{\"userId\":5,\"roleIds\": [1,2]}");
    }

    /**
     * 修改只读
     */
    @Test
    @Rollback()
    @Transactional
    public void changeReadonly() throws Exception {
        mockPost("/api/v1/user/change/readonly", "{\"userId\":5,\"readOnly\":1}");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/user/export", "{\"page\":1,\"pageSize\":1}", "target/user.xlsx");
    }

    /**
     * 导出模板
     */
    @Test
    public void exportTemplate() throws Exception {
        mockExport("/api/v1/user/export/template", null, "target/template.xlsx");
    }

    /**
     * 导入用户
     */
    @Test
    @Rollback()
    @Transactional
    public void importUser() throws Exception {
        mockImport("/api/v1/user/import", null, "source/user-import.xlsx");
    }
}
