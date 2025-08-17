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
import com.cowave.hub.admin.app.sys.HubDictController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
public class HubDictControllerTest extends SpringTest {

    @Autowired
    private HubDictController hubDictController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(hubDictController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 刷新缓存
     */
    @Test
    public void refresh() throws Exception {
        mockGet("/api/v1/dict/refresh");
    }

    /**
     * 获取字典
     */
    @Test
    public void cacheDict() throws Exception {
        mockGet("/api/v1/dict/cache/dict?dictCode=yes");
    }

    /**
     * 获取类型字典
     */
    @Test
    public void cacheType() throws Exception {
        mockGet("/api/v1/dict/cache/type?typeCode=yes_no");
    }

    /**
     * 获取分组字典
     */
    @Test
    public void cacheGroup() throws Exception {
        mockGet("/api/v1/dict/cache/group?groupCode=sys");
    }

    /**
     * 字典类型选项
     */
    @Test
    public void dictOptions() throws Exception {
        mockGet("/api/v1/dict/options");
    }

    /**
     * Group子类型选项
     */
    @Test
    public void groupOptions() throws Exception {
        mockGet("/api/v1/dict/group/options?groupCode=sys");
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockGet("/api/v1/dict/list?groupCode=sys&typeCode=yes_no");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/dict/info/5");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/dict/add", "{\"typeCode\":\"yes_no\",\"dictCode\":\"sys_test\",\"dictValue\":1,\"status\":1,\"dictOrder\":29," +
                "\"dictLabel\":\"测试字典\",\"valueParser\":\"com.cowave.commons.framework.helper.redis.dict.DefaultValueParser\",\"valueParam\":\"int\"}");
    }

    /**
     * 修改
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/dict/edit", "{\"id\":6,\"typeCode\":\"sys\",\"dictCode\":\"sys_no_yes\",\"dictValue\":1,\"status\":1,\"dictOrder\":29," +
                "\"dictLabel\":\"测试字典\",\"valueParser\":\"com.cowave.commons.framework.helper.redis.dict.DefaultValueParser\",\"valueParam\":\"int\"}");
    }

    /**
     * 删除
     */
    @Test
    @Rollback()
    @Transactional
    public void delete() throws Exception {
        mockGet("/api/v1/dict/delete?dictId=7,8");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/dict/export", null, "target/dict.xlsx");
    }
}
