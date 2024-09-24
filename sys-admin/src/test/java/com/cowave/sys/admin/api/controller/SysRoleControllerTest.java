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
import com.cowave.sys.admin.api.controller.sys.SysRoleController;
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
public class SysRoleControllerTest extends SpringTest {

    @Autowired
    private SysRoleController sysRoleController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysRoleController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/role/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/role/info/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/role/add", "{\"roleCode\":\"testRole\",\"roleName\":\"测试角色\"}");
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/role/edit", "{\"roleId\":3,\"roleCode\":\"testRole\",\"roleName\":\"测试角色\"}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/role/delete?roleId=3");
    }

    /**
     * 修改只读
     */
    @Test
    @Rollback()
    @Transactional
    public void changeReadonly() throws Exception {
        mockPost("/api/v1/role/change/readonly", "{\"roleId\":3,\"readOnly\":1}");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/role/export", "{\"page\":1,\"pageSize\":1}", "target/role.xlsx");
    }

    /**
     * 修改角色菜单
     */
    @Test
    @Rollback()
    @Transactional
    public void changeMenus() throws Exception {
        mockPost("/api/v1/role/change/menus", "{\"roleId\":3,\"menuIds\":[6,7,8,9]}");
    }

    /**
     * 已授权用户
     */
    @Test
    public void userAuthed() throws Exception {
        mockPost("/api/v1/role/user/authed", "{\"roleId\":3,\"userName\":\"刘\"}");
    }

    /**
     * 未授权用户
     */
    @Test
    public void userUnAuthed() throws Exception {
        mockPost("/api/v1/role/user/unauthed", "{\"roleId\":1,\"userName\":\"刘\"}");
    }

    /**
     * 授予用户角色
     */
    @Test
    @Rollback()
    @Transactional
    public void grant() throws Exception {
        mockPost("/api/v1/role/user/grant", "{\"roleId\":1,\"userIds\":[3,4,5]}");
    }

    /**
     * 取消用户角色
     */
    @Test
    @Rollback()
    @Transactional
    public void cancel() throws Exception {
        mockPost("/api/v1/role/user/cancel", "{\"roleId\":3,\"userIds\":[3,4,5]}");
    }
}
