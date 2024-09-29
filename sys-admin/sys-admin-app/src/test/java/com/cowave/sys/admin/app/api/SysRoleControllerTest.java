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
import com.cowave.sys.admin.app.rabc.SysRoleController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class SysRoleControllerTest extends SpringTest {

    @Autowired
    private SysRoleController sysRoleController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysRoleController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
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
        mockPost("/api/v1/role/users/authed", "{\"roleId\":3,\"userName\":\"刘\"}");
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
