/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.rabc;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.rabc.HubScope;
import com.cowave.hub.admin.domain.rabc.request.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rabc.request.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rabc.request.ScopeQuery;
import com.cowave.hub.admin.domain.rabc.request.ScopeStatusUpdate;
import com.cowave.hub.admin.service.rabc.HubScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据权限
 * @order 13
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scope")
public class HubScopeController {

    private final HubScopeService hubScopeService;

    /**
	 * 列表
	 */
	@PreAuthorize("@permits.hasPermit('hub:scope:query')")
	@GetMapping
	public Response<Response.Page<HubScope>> list(ScopeQuery query) {
		return Response.page(hubScopeService.page(Access.tenantId(), query));
	}

    /**
	 * 详情
	 */
	@PreAuthorize("@permits.hasPermit('hub:scope:query')")
	@GetMapping("/{scopeId}")
	public Response<HubScope> info(@PathVariable Integer scopeId) {
		return Response.success(hubScopeService.info(Access.tenantId(), scopeId));
	}

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:create')")
    @PostMapping
    public Response<Void> create(@RequestBody HubScope hubScope) throws Exception {
        return Response.success(() -> hubScopeService.create(Access.tenantId(), hubScope));
    }

    /**
     * 删除
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:delete')")
    @DeleteMapping("/{scopeIds}")
    public Response<Void> delete(@PathVariable List<Integer> scopeIds) throws Exception {
        return Response.success(() -> hubScopeService.delete(Access.tenantId(), scopeIds));
    }

    /**
     * 修改
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody ScopeNameUpdate nameUpdate) throws Exception {
        return Response.success(() -> hubScopeService.edit(Access.tenantId(), nameUpdate));
    }

    /**
     * 修改状态
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping("/status")
    public Response<Void> switchStatus(@RequestBody ScopeStatusUpdate statusUpdate) throws Exception {
        return Response.success(() -> hubScopeService.switchStatus(Access.tenantId(), statusUpdate));
    }

    /**
     * 修改权限内容
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping("/content")
    public Response<Void> editContent(@RequestBody ScopeInfoUpdate infoUpdate) throws Exception {
        return Response.success(() -> hubScopeService.editContent(Access.tenantId(), infoUpdate));
    }
}
