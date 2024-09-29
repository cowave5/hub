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
import com.cowave.sys.admin.app.rabc.SysMenuController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class SysMenuControllerTest extends SpringTest {

    @Autowired
    private SysMenuController sysMenuController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysMenuController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
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
