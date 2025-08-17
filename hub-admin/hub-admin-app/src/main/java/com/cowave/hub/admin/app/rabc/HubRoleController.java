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

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.rabc.HubRole;
import com.cowave.hub.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.hub.admin.domain.rabc.request.*;
import com.cowave.hub.admin.service.rabc.HubRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.OpAction.*;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_ROLE;

/**
 * 角色
 * @order 5
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class HubRoleController {

    private final HubRoleService hubRoleService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('hub:role:query')")
    @GetMapping
    public Response<Response.Page<HubRole>> list(RoleQuery query) {
        return Response.page(hubRoleService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     */
    @PreAuthorize("@permits.hasPermit('hub:role:query')")
    @GetMapping("/{roleId}")
    public Response<RoleInfoDto> info(@PathVariable Integer roleId) {
        return Response.success(hubRoleService.info(Access.tenantId(), roleId));
    }

    /**
     * 新增
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = CREATE, desc = "新增角色：#{#role.roleName}")
    @PreAuthorize("@permits.hasPermit('hub:role:create')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody HubRole role) throws Exception {
        return Response.success(() -> hubRoleService.add(Access.tenantId(), role));
    }

    /**
     * 删除
     *
     * @param roleIds 角色id列表
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = DELETE, desc = "删除角色")
    @PreAuthorize("@permits.hasPermit('hub:role:delete')")
    @DeleteMapping("/{roleIds}")
    public Response<Void> delete(@PathVariable List<Integer> roleIds) throws Exception {
        return Response.success(() -> hubRoleService.delete(Access.tenantId(), roleIds));
    }

    /**
     * 修改
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = EDIT, desc = "修改角色：#{#role.roleName}")
    @PreAuthorize("@permits.hasPermit('hub:role:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody HubRole role) throws Exception {
        return Response.success(() -> hubRoleService.edit(Access.tenantId(), role));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@permits.hasPermit('hub:role:menus')")
    @PatchMapping("/menus")
    public Response<Void> updateMenus(@RequestBody RoleMenuUpdate roleUpdate) throws Exception {
        return Response.success(() -> hubRoleService.updateMenus(Access.tenantId(), roleUpdate));
    }

    /**
     * 导出角色
     */
    @PreAuthorize("@permits.hasPermit('hub:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoleQuery query) throws IOException {
    	String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<HubRole> roleList = hubRoleService.list(Access.tenantId(), query).getRecords();
		EasyExcel.write(response.getOutputStream(), HubRole.class)
		.sheet("角色").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(roleList);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:grant')")
    @PostMapping("/user/grant")
    public Response<Void> grantUser(@Validated @RequestBody RoleUserUpdate roleUpdate) throws Exception {
    	return Response.success(() -> hubRoleService.grantUser(Access.tenantId(), roleUpdate));
    }

    /**
     * 取消用户
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:cancel')")
    @PostMapping("/user/cancel")
    public Response<Void> cancelUser(@Validated @RequestBody RoleUserUpdate roleUpdate) throws Exception {
    	return Response.success(() -> hubRoleService.cancelUser(Access.tenantId(), roleUpdate));
    }

    /**
     * 用户列表（已授权）
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:query')")
    @GetMapping("/users/authed")
    public Response<Response.Page<RoleUserDto>> getAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(hubRoleService.getAuthedUser(Access.tenantId(), query));
    }

    /**
     * 用户列表（未授权）
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:query')")
    @GetMapping("/users/unAuthed")
    public Response<Response.Page<RoleUserDto>> getUnAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(hubRoleService.getUnAuthedUser(Access.tenantId(), query));
    }

    /**
     * 角色名称查询
     */
    @GetMapping("/name/{roleIds}")
    public Response<List<String>> getNames(@PathVariable List<Integer> roleIds) {
        return Response.success(hubRoleService.getNames(Access.tenantId(), roleIds));
    }
}
