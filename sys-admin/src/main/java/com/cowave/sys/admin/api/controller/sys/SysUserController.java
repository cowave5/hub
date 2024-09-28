/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.sys;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.framework.helper.operation.Operation;
import com.cowave.commons.framework.helper.operation.Operation.Content;
import com.cowave.commons.framework.support.excel.valid.ExcelImportValidListener;
import com.cowave.commons.framework.support.excel.write.DropdownWriteHandler;
import com.cowave.commons.tools.Messages;
import com.cowave.sys.admin.api.caches.SysUserCaches;
import com.cowave.sys.admin.api.service.SysUserService;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.model.admin.SysUser;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController {

	private final SysUserCaches sysUserCaches;

	private final SysUserService sysUserService;

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysUserCaches.refreshTree();
		sysUserCaches.refreshDeptUserTree();
		return Response.success();
	}

	/**
	 * 人员关系
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:diagram')")
	@GetMapping("/tree")
	public Response<Tree<String>> tree() {
		return Response.success(sysUserCaches.tree());
	}

	/**
	 * 部门用户树
	 */
	@GetMapping("/tree/dept")
	public Response<List<Tree<String>>> deptUserTree() {
		return Response.success(List.of(sysUserCaches.deptUserTree()));
	}

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:query')")
	@PostMapping("/list")
	public Response<Response.Page<SysUser>> list(@RequestBody SysUser sysUser) {
		return Response.success(new Response.Page<>(sysUserService.list(sysUser), sysUserService.count(sysUser)));
	}

	/**
	 * 详情
	 * @param userId 用户id
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:query')")
	@GetMapping(value = {"/info/{userId}"})
	public Response<SysUser> info(@PathVariable Long userId) {
		return Response.success(sysUserService.info(userId));
	}

	/**
	 * 新增
	 */
	@Operation(type = "admin_user", action = "add", desc = "新增用户：#{sysUser.userName}", content = Content.REQ)
	@PreAuthorize("@permit.hasPermit('sys:user:new')")
	@PostMapping("/add")
	public Response<Void> add(@Validated @RequestBody SysUser sysUser) {
		sysUserService.add(sysUser);
		return Response.success();
	}

	/**
	 * 编辑
	 */
	@Operation(type = "admin_user", action = "edit", desc = "修改用户：#{resp.userName}", content = Content.ALL)
	@PreAuthorize("@permit.hasPermit('sys:user:edit')")
	@PostMapping("/edit")
	public Response<SysUser> edit(@Validated @RequestBody SysUser sysUser) {
		return Response.success(sysUserService.edit(sysUser));
	}

	/**
	 * 删除
	 * @param userId 用户id
	 */
	@Operation(type = "admin_user", action = "delete", desc = "删除用户", content = Content.RESP)
	@PreAuthorize("@permit.hasPermit('sys:user:delete')")
	@GetMapping(value = {"/delete"})
	public Response<List<SysUser>> delete(@NotNull(message = "{user.notnull.id}") Long[] userId) {
		return Response.success(sysUserService.delete(userId));
	}

	/**
	 * 修改状态
	 */
	@Operation(type = "admin_user", action = "status", desc = "修改用户[#{sysUser.userName}]状态", content = Content.REQ)
	@PreAuthorize("@permit.hasPermit('sys:user:edit')")
	@PostMapping("/change/status")
	public Response<Void> changeStatus(@RequestBody SysUser sysUser) {
		sysUserService.changeStatus(sysUser);
		return Response.success();
	}

	/**
	 * 修改密码
	 */
	@Operation(type = "admin_user", action = "passwd", desc = "修改用户[#{sysUser.userName}]密码")
	@PreAuthorize("@permit.hasPermit('sys:user:passwd')")
	@PostMapping("/change/passwd")
	public Response<Void> changePasswd(@RequestBody SysUser sysUser) {
		sysUserService.changePasswd(sysUser);
		return Response.success();
	}

	/**
	 * 修改角色
	 */
	@Operation(type = "admin_user", action = "role_grant", desc = "修改用户[#{sysUser.userName}]角色")
	@PreAuthorize("@permit.hasPermit('sys:user:grant')")
	@PostMapping("/change/roles")
	public Response<Void> changeRoles(@RequestBody SysUser sysUser) {
		sysUserService.changeRoles(sysUser);
		return Response.success();
	}

	/**
	 * 修改只读
	 */
	@Operation(type = "admin_user", action = "readonly", desc = "修改用户[#{sysUser.userName}]只读状态", content = Content.REQ)
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysUser sysUser) {
		sysUserService.changeReadonly(sysUser);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:export')")
	@RequestMapping("/export")
	public void export(HttpServletResponse response, SysUser sysUser) throws Exception {
		String fileName = URLEncoder.encode("用户数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysUser.class)
				.sheet("用户").registerWriteHandler(new CellWidthHandler()).doWrite(sysUserService.list(sysUser));
	}

	/**
	 * 导出模板
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:export')")
	@RequestMapping("/export/template")
	public void exportTemplate(HttpServletResponse response) throws Exception {
		String fileName = URLEncoder.encode("test", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		DropdownWriteHandler sexHandler =
				new DropdownWriteHandler(List.of("男", "女", "未知"), 3, 1000);
		DropdownWriteHandler statusHandler =
				new DropdownWriteHandler(List.of("启用", "停用"), 6, 1000);
		EasyExcel.write(response.getOutputStream(), SysUser.class)
				.sheet("用户")
				.registerWriteHandler(sexHandler)
				.registerWriteHandler(statusHandler)
				.doWrite(new ArrayList<>());
	}

	/**
	 * 导入用户
	 */
	@PreAuthorize("@permit.hasPermit('sys:user:import')")
	@PostMapping("/import")
	public Response<Void> importUser(MultipartFile file, boolean updateSupport) throws Exception {
		try(InputStream inputStream = file.getInputStream()){
			EasyExcel.read(inputStream).head(SysUser.class).registerReadListener(
					new ExcelImportValidListener<>(sysUserService, updateSupport)).sheet().doReadSync();
		}
		return Response.success(null, Messages.msg("import.success.msg"));
	}

	/**
	 * 候选人: 用户Leaders
	 */
	@GetMapping("/leaders")
	public Response<List<SysUser>> leaders(Long userId) {
		return Response.success(sysUserService.leaders(userId));
	}
}
