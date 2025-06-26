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

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.sys.admin.domain.rabc.SysRole;
import com.cowave.sys.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.service.rabc.SysRoleService;
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

/**
 * 角色
 * @order 4
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:role:query')")
    @GetMapping
    public Response<Response.Page<SysRole>> list(RoleQuery query) {
        return Response.page(sysRoleService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     */
    @PreAuthorize("@permit.hasPermit('sys:role:query')")
    @GetMapping("/{roleId}")
    public Response<RoleInfoDto> info(@PathVariable Integer roleId) {
        return Response.success(sysRoleService.info(Access.tenantId(), roleId));
    }

    /**
     * 新增
     */
    @Operation(module = "op_admin", type = "op_role", action = "op_create", desc = "新增角色：#{#sysRole.roleName}")
    @PreAuthorize("@permit.hasPermit('sys:role:create')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysRole sysRole) throws Exception {
        return Response.success(() -> sysRoleService.add(Access.tenantId(), sysRole));
    }

    /**
     * 删除
     *
     * @param roleIds 角色id列表
     */
    @Operation(module = "op_admin", type = "op_role", action = "op_delete", desc = "删除角色")
    @PreAuthorize("@permit.hasPermit('sys:role:delete')")
    @DeleteMapping("/{roleIds}")
    public Response<Void> delete(@PathVariable List<Integer> roleIds) throws Exception {
        return Response.success(() -> sysRoleService.delete(Access.tenantId(), roleIds));
    }

    /**
     * 修改
     */
    @Operation(module = "op_admin", type = "op_role", action = "op_edit", desc = "修改角色：#{#sysRole.roleName}")
    @PreAuthorize("@permit.hasPermit('sys:role:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody SysRole sysRole) throws Exception {
        return Response.success(() -> sysRoleService.edit(Access.tenantId(), sysRole));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@permit.hasPermit('sys:role:menus')")
    @PatchMapping("/menus")
    public Response<Void> updateMenus(@RequestBody RoleMenuUpdate roleUpdate) throws Exception {
        return Response.success(() -> sysRoleService.updateMenus(Access.tenantId(), roleUpdate));
    }

    /**
     * 导出角色
     */
    @PreAuthorize("@permit.hasPermit('sys:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoleQuery query) throws IOException {
    	String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<SysRole> roleList = sysRoleService.list(Access.tenantId(), query).getRecords();
		EasyExcel.write(response.getOutputStream(), SysRole.class)
		.sheet("角色").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(roleList);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@permit.hasPermit('sys:role:members:grant')")
    @PostMapping("/user/grant")
    public Response<Void> grantUser(@Validated @RequestBody RoleUserUpdate roleUpdate) throws Exception {
    	return Response.success(() -> sysRoleService.grantUser(Access.tenantId(), roleUpdate));
    }

    /**
     * 取消用户
     */
    @PreAuthorize("@permit.hasPermit('sys:role:members:cancel')")
    @PostMapping("/user/cancel")
    public Response<Void> cancelUser(@Validated @RequestBody RoleUserUpdate roleUpdate) throws Exception {
    	return Response.success(() -> sysRoleService.cancelUser(Access.tenantId(), roleUpdate));
    }

    /**
     * 用户列表（已授权）
     */
    @PreAuthorize("@permit.hasPermit('sys:role:members:query')")
    @GetMapping("/users/authed")
    public Response<Response.Page<RoleUserDto>> getAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(sysRoleService.getAuthedUser(Access.tenantId(), query));
    }

    /**
     * 用户列表（未授权）
     */
    @PreAuthorize("@permit.hasPermit('sys:role:members:query')")
    @GetMapping("/users/unAuthed")
    public Response<Response.Page<RoleUserDto>> getUnAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(sysRoleService.getUnAuthedUser(Access.tenantId(), query));
    }

    /**
     * 角色名称查询
     */
    @GetMapping("/name/{roleIds}")
    public Response<List<String>> getNames(@PathVariable List<Integer> roleIds) {
        return Response.success(sysRoleService.getNames(Access.tenantId(), roleIds));
    }
}
