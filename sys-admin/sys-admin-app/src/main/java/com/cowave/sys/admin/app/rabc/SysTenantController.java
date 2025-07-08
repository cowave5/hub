/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.rabc;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.domain.rabc.dto.TenantManager;
import com.cowave.sys.admin.service.rabc.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户
 * @order 0
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tenant")
public class SysTenantController {

    private final SysTenantService sysTenantService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:query')")
    @GetMapping
    public Response<Response.Page<SysTenant>> list(TenantQuery query) {
        return Response.page(sysTenantService.page(query));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:query')")
    @GetMapping("/{tenantId}")
    public Response<SysTenant> info(@PathVariable String tenantId) {
        return Response.success(sysTenantService.info(tenantId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:creat')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody TenantCreate tenantCreate) throws Exception {
        return Response.success(() -> sysTenantService.create(tenantCreate));
    }

    /**
     * 修改
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody TenantCreate tenantCreate) throws Exception {
        return Response.success(() -> sysTenantService.edit(tenantCreate));
    }

    /**
     * 修改状态
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:status')")
    @PatchMapping("/status")
    public Response<Void> updateStatus(@RequestBody TenantStatusUpdate statusUpdate) throws Exception {
        return Response.success(() -> sysTenantService.updateStatus(statusUpdate));
    }

    /**
     * 管理员列表
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:query')")
    @GetMapping("/manager/{tenantId}")
    public Response<Response.Page<TenantManager>> listManager(@PathVariable String tenantId) {
        return Response.page(sysTenantService.listManager(tenantId));
    }

    /**
     * 创建管理员
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:create')")
    @PostMapping("/manager")
    public Response<Void> createManager(@Validated @RequestBody TenantManagerCreate managerCreate) throws Exception {
        return Response.success(() -> sysTenantService.createManager(managerCreate));
    }

    /**
     * 移除管理员
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:remove')")
    @PatchMapping("/manager/remove")
    public Response<Void> removeManager(@Validated @RequestBody TenantManagerRemove managerRemove) throws Exception {
        return Response.success(() -> sysTenantService.removeManager(managerRemove));
    }

    /**
     * 租户选项
     */
    @GetMapping("/options")
    public Response<List<SelectOption>> tenantOptions() {
        return Response.success(sysTenantService.tenantOptions());
    }
}
