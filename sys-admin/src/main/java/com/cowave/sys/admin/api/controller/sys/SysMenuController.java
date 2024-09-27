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

import com.cowave.sys.admin.api.service.SysMenuService;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.admin.api.entity.RoleAuthed;
import com.cowave.sys.model.admin.SysMenu;
import com.cowave.sys.model.admin.SysRole;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import cn.hutool.core.lang.tree.Tree;
import lombok.RequiredArgsConstructor;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class SysMenuController{

	private final SysMenuService sysMenuService;

	/**
	 * 树
	 */
	@GetMapping("/tree")
	public Response<List<Tree<Long>>> tree(){
		return Response.success(sysMenuService.tree());
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public Response<Response.Page<SysMenu>> list(String menuName, Integer menuStatus){
		return Response.page(sysMenuService.list(menuName, menuStatus, null, false));
	}

	/**
     * 详情
     *
     * @param menuId 菜单id
     */
    @GetMapping(value = "/info/{menuId}")
    public Response<SysMenu> info(@PathVariable Long menuId) {
        return Response.success(sysMenuService.info(menuId));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Long> add(@Validated @RequestBody SysMenu sysMenu) {
    	sysMenuService.add(sysMenu);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody SysMenu sysMenu) {
    	sysMenuService.edit(sysMenu);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param menuId 菜单id
     */
    @GetMapping("/delete")
    public Response<Void> delete(@NotNull(message = "{menu.notnull.id}") Long menuId) {
    	sysMenuService.delete(menuId);
        return Response.success();
    }

	/**
	 * 修改只读
	 */
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysMenu sysMenu) {
		sysMenuService.changeReadonly(sysMenu);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@RequestMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("菜单数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysMenu.class)
		.sheet("菜单").registerWriteHandler(new CellWidthHandler()).doWrite(sysMenuService.list(null, null, null, false));
	}

	/**
     * 已授权角色
     */
	@PostMapping("/role/authed")
    public Response<Response.Page<SysRole>> roleAuthed(@Validated @RequestBody RoleAuthed roleAuthed) {
    	return Response.page(sysMenuService.roleAuthed(roleAuthed));
    }

    /**
     * 未授权角色
     */
	@PostMapping("/role/unauthed")
    public Response<Response.Page<SysRole>> roleUnAuthed(@Validated @RequestBody RoleAuthed roleAuthed) {
    	return Response.page(sysMenuService.roleUnAuthed(roleAuthed));
    }

    /**
     * 授予角色菜单
     */
    @PostMapping("/role/grant")
    public Response<Void> grant(@Validated @RequestBody RoleAuthed roleAuthed) {
        sysMenuService.grant(roleAuthed);
    	return Response.success();
    }

    /**
     * 取消角色菜单
     */
    @PostMapping("/role/cancel")
    public Response<Void> cancel(@Validated @RequestBody RoleAuthed roleAuthed) {
        sysMenuService.cancel(roleAuthed);
    	return Response.success();
    }
}
