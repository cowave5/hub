/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.domain.rabc.request.ScopeInfoUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeNameUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeQuery;
import com.cowave.sys.admin.domain.rabc.request.ScopeStatusUpdate;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysScopeService {

    /**
     * 列表
     */
    Page<SysScope> page(String tenantId, ScopeQuery query);

    /**
     * 详情
     */
    SysScope info(String tenantId, Integer scopeId);

    /**
     * 新增
     */
    void create(String tenantId, SysScope sysScope);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> scopeIds);

    /**
     * 修改
     */
    void edit(String tenantId, ScopeNameUpdate nameUpdate);

    /**
     * 修改状态
     */
    void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate);

    /**
     * 修改权限
     */
    void editContent(String tenantId, ScopeInfoUpdate infoUpdate);
}
