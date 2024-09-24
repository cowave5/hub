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
import com.cowave.sys.admin.api.controller.sys.SysMenuController;
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
public class SysMenuControllerTest extends SpringTest {

    @Autowired
    private SysMenuController sysMenuController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysMenuController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 树
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/menu/tree");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/menu/list");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/menu/info/5");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/menu/add", "{\"menuName\":\"测试菜单\",\"menuType\":\"M\"}");
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/menu/edit", "{\"menuId\":4,\"menuName\":\"测试菜单\",\"menuType\":\"M\"}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/menu/delete?menuId=20");
    }

    /**
     * 修改只读
     */
    @Test
    @Rollback()
    @Transactional
    public void changeReadonly() throws Exception {
        mockPost("/api/v1/menu/change/readonly", "{\"menuId\":20,\"readOnly\":1}");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/menu/export", null, "target/menu.xlsx");
    }

    /**
     * 已授权角色
     */
    @Test
    public void roleAuthed() throws Exception {
        mockPost("/api/v1/menu/role/authed", "{\"menuId\":10}");
    }

    /**
     * 未授权角色
     */
    @Test
    public void roleUnAuthed() throws Exception {
        mockPost("/api/v1/menu/role/unauthed", "{\"menuId\":10}");
    }

    /**
     * 授予角色菜单
     */
    @Test
    public void grant() throws Exception {
        mockPost("/api/v1/menu/role/grant", "{\"menuId\":10,\"roleIds\":[3]}");
    }

    /**
     * 取消角色菜单
     */
    @Test
    public void cancel() throws Exception {
        mockPost("/api/v1/menu/role/cancel", "{\"menuId\":10,\"roleIds\":[3]}");
    }
}
