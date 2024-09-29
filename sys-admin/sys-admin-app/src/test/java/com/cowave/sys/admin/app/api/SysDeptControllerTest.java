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
import com.cowave.sys.admin.app.rabc.SysDeptController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class SysDeptControllerTest extends SpringTest {

    @Autowired
    private SysDeptController sysDeptController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysDeptController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/dept?page=1&pageSize=2");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/dept/2");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void create() throws Exception {
        mockPost("/api/v1/dept",
                """
                        {
                            "deptName":"测试部门",
                            "parentIds":[4]
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
        mockPatch("/api/v1/dept",
                """
                        {
                            "deptId":8,
                            "deptName":"测试部门",
                            "parentIds":[4]
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
        mockDelete("/api/v1/dept/4,5");
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
     * 成员
     */
    @Test
    public void listWithDept() throws Exception {
        mockGet("/api/v1/dept/members/joined/1?page=1&pageSize=2");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/dept/export", "{\"page\":1,\"pageSize\":1}", "target/dept.xlsx");
    }

    /**
     * 获取部门岗位
     */
    @Test
    public void getPosts() throws Exception {
        mockGet("/api/v1/dept/posts/configured/6");
    }

    /**
     * 设置部门岗位
     */
    @Test
    @Rollback()
    @Transactional
    public void setPosts() throws Exception {
        mockPost("/api/v1/dept/posts", "[{\"deptId\":4,\"postId\":3,\"isDefault\":1},{\"deptId\":4,\"postId\":4,\"isDefault\":0}]");
    }

    /**
     * 获取部门人员
     */
    @Test
    public void getUsers() throws Exception {
        mockGet("/api/v1/dept/members/joined/6");
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
