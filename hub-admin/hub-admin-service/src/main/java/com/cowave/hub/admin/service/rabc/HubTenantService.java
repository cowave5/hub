/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.vo.SelectOption;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.dto.TenantManager;
import com.cowave.hub.admin.domain.rabc.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubTenantService {

    /**
     * 列表
     */
    Page<HubTenant> page(TenantQuery query);

    /**
     * 详情
     */
    HubTenant info(String tenantId);

    /**
     * 新增
     */
    void create(TenantCreate tenantCreate);

    /**
     * 修改
     */
    void edit(TenantCreate tenantCreate);

    /**
     * 修改状态
     */
    void updateStatus(TenantStatusUpdate statusUpdate);

    /**
     * 管理员列表
     */
    Page<TenantManager> listManager(String tenantId);

    /**
     * 创建管理员
     */
    void createManager(TenantManagerCreate managerCreate);

    /**
     * 移除管理员
     */
    void removeManager(TenantManagerRemove managerRemove);

    /**
     * 租户选项
     */
    List<SelectOption> tenantOptions();
}
