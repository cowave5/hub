/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.api;

import com.cowave.commons.framework.access.filter.AccessFilter;
import com.cowave.commons.framework.access.security.BearerTokenFilter;
import com.cowave.hub.admin.app.SpringTest;
import com.cowave.hub.admin.app.rabc.HubUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class HubUserControllerTest extends SpringTest {

    @Autowired
    private HubUserController hubUserController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(hubUserController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/user?page=1&pageSize=2");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/user/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void create() throws Exception {
        mockPost("/api/v1/user",
                """
                        {
                            "userAccount": "test",
                            "userName": "测试人员",
                            "userPasswd": "123456"
                        }
                        """
        );
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPatch("/api/v1/user",
                """ 
                        {
                            "userId":5,
                            "userAccount":"test",
                            "userName":"测试人员",
                            "userPasswd":"123456"
                        }
                        """
        );
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockDelete("/api/v1/user/3,4");
    }

    /**
     * 修改状态
     */
    @Test
    @Rollback()
    @Transactional
    public void changeStatus() throws Exception {
        mockPatch("/api/v1/user/status",
                """
                        {
                            "userId":5,
                            "userName":"test",
                            "userStatus":0
                        }
                        """
        );
    }

    /**
     * 修改密码
     */
    @Test
    @Rollback()
    @Transactional
    public void changePasswd() throws Exception {
        mockPatch("/api/v1/user/passwd",
                """
                        {
                            "userId":5,
                            "userName":"test",
                            "userPasswd":"123123"
                        }
                        """
        );
    }

    /**
     * 修改角色
     */
    @Test
    @Rollback()
    @Transactional
    public void changeRoles() throws Exception {
        mockPatch("/api/v1/user/roles",
                """
                        {
                            "userId":5,
                            "userName":"test",
                            "roleIds": [1,2]
                        }
                        """
        );
    }

    /**
     * 导出用户
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

    /**
     * 刷新缓存
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/user/refresh");
    }

    /**
     * 用户树
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
}
