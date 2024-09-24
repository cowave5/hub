/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.sys;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.cowave.commons.framework.helper.operation.Operation;
import com.cowave.commons.framework.helper.operation.Operation.Content;
import com.cowave.sys.admin.api.service.SysRoleService;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.admin.api.entity.UserAuthed;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;

/**
 * 角色
 *
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
    @PostMapping("/list")
    public Response<Response.Page<SysRole>> list(@RequestBody SysRole sysRole) {
        return Response.page(sysRoleService.list(sysRole));
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     */
    @GetMapping(value = "/info/{roleId}")
    public Response<SysRole> info(@PathVariable Long roleId) {
        return Response.success(sysRoleService.info(roleId));
    }

    /**
     * 新增
     */
    @Operation(type = "admin_role", action = "add", desc = "新增角色：#{sysRole.roleName}", content = Content.REQ)
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysRole sysRole) {
    	sysRoleService.add(sysRole);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(type = "admin_role", action = "edit", desc = "修改角色：#{resp.roleName}", content = Content.ALL)
    @PostMapping("/edit")
    public Response<SysRole> edit(@Validated @RequestBody SysRole sysRole) {
        return Response.success(sysRoleService.edit(sysRole));
    }

    /**
     * 删除
     *
     * @param roleId 角色id
     */
    @Operation(type = "admin_role", action = "delete", desc = "删除角色", content = Content.RESP)
    @GetMapping("/delete")
    public Response<List<SysRole>> delete(@NotNull(message = "role.notnull.id") Long[] roleId) {
        return Response.success(sysRoleService.delete(roleId));
    }

    /**
     * 修改只读
     */
    @Operation(type = "admin_role", action = "readonly", desc = "修改角色[#{sysRole.roleName}]只读状态", content = Content.REQ)
    @PreAuthorize("@permit.hasPermit('sys:common:readonly')")
    @PostMapping("/change/readonly")
    public Response<Void> changeReadonly(@RequestBody SysRole sysRole) {
        sysRoleService.changeReadonly(sysRole);
        return Response.success();
    }

    /**
     * 导出
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
    	String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysRole.class)
		.sheet("角色").registerWriteHandler(new CellWidthHandler()).doWrite(sysRoleService.list(new SysRole()).getRecords());
    }

    /**
     * 修改角色菜单
     */
    @PostMapping("/change/menus")
    public Response<Void> changeMenus(@RequestBody SysRole sysRole) {
        sysRoleService.changeMenus(sysRole);
        return Response.success();
    }

    /**
     * 已授权用户
     */
    @PostMapping("/user/authed")
    public Response<Response.Page<SysUser>> userAuthed(@Validated @RequestBody UserAuthed userAuthed) {
    	return Response.page(sysRoleService.userAuthed(userAuthed));
    }

    /**
     * 未授权用户
     */
    @PostMapping("/user/unauthed")
    public Response<Response.Page<SysUser>> userUnAuthed(@Validated @RequestBody UserAuthed userAuthed) {
    	return Response.page(sysRoleService.userUnAuthed(userAuthed));
    }

    /**
     * 授予用户角色
     */
    @PostMapping("/user/grant")
    public Response<Void> grant(@Validated @RequestBody UserAuthed userAuthed) {
        sysRoleService.grant(userAuthed);
    	return Response.success();
    }

    /**
     * 取消用户角色
     */
    @PostMapping("/user/cancel")
    public Response<Void> cancel(@Validated @RequestBody UserAuthed userAuthed) {
        sysRoleService.cancel(userAuthed);
    	return Response.success();
    }
}
