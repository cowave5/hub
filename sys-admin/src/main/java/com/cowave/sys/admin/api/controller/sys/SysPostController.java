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

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.framework.helper.operation.Operation;
import com.cowave.commons.framework.helper.operation.Operation.Content;
import com.cowave.sys.admin.api.caches.SysPostCaches;
import com.cowave.sys.admin.api.service.SysPostService;
import com.cowave.sys.model.admin.SysUser;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.model.admin.SysPost;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;

/**
 * 岗位
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class SysPostController {

	private final SysPostCaches sysPostCaches;

	private final SysPostService sysPostService;

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysPostCaches.refreshTree();
		sysPostCaches.refreshDeptPostTree();
		return Response.success();
	}

	/**
	 * 岗位关系
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:diagram')")
	@GetMapping("/tree")
	public Response<Tree<String>> tree() {
		return Response.success(sysPostCaches.tree());
	}

	/**
	 * 部门岗位树
	 */
	@GetMapping("/tree/dept")
	public Response<List<Tree<String>>> deptPostTree() {
		return Response.success(List.of(sysPostCaches.deptPostTree()));
	}

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@PostMapping("/list")
	public Response<Response.Page<SysPost>> list(@RequestBody SysPost sysPost) {
		return Response.page(sysPostService.list(sysPost));
	}

	/**
	 * 详情
	 * @param postId 岗位id
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@GetMapping(value = {"/info/{postId}"})
	public Response<SysPost> info(@PathVariable Long postId) {
		return Response.success(sysPostService.info(postId));
	}

	/**
	 * 新增
	 */
	@Operation(type = "admin_post", action = "add", desc = "新增岗位：#{sysPost.postName}", content = Content.REQ)
	@PreAuthorize("@permit.hasPermit('sys:post:new')")
	@PostMapping("/add")
	public Response<Void> add(@Validated @RequestBody SysPost sysPost) {
		sysPostService.add(sysPost);
		return Response.success();
	}

	/**
	 * 修改
	 */
	@Operation(type = "admin_post", action = "edit", desc = "修改岗位：#{resp.postName}", content = Content.ALL)
	@PreAuthorize("@permit.hasPermit('sys:post:edit')")
	@PostMapping("/edit")
	public Response<SysPost> edit(@Validated @RequestBody SysPost sysPost) {
		return Response.success(sysPostService.edit(sysPost));
	}

	/**
	 * 删除
	 *
	 * @param postId 岗位id
	 */
	@Operation(type = "admin_post", action = "delete", desc = "删除岗位", content = Content.RESP)
	@PreAuthorize("@permit.hasPermit('sys:post:delete')")
	@GetMapping("/delete")
	public Response<List<SysPost>> delete(@NotNull(message = "{post.notnull.id}") Long[] postId) {
		return Response.success(sysPostService.delete(postId));
	}

	/**
	 * 修改只读
	 */
	@Operation(type = "admin_post", action = "readonly", desc = "修改岗位[#{sysPost.postName}]只读状态", content = Content.REQ)
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysPost sysPost) {
		sysPostService.changeReadonly(sysPost);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:export')")
	@RequestMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("岗位数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysPost.class)
		.sheet("岗位").registerWriteHandler(new CellWidthHandler()).doWrite(sysPostService.list(new SysPost()).getRecords());
	}

	/**
	 * 岗位人员，code获取
	 * @param postCode 岗位编码
	 */
	@GetMapping("/users/code/{postCode}")
	public Response<List<SysUser>> getUsersByCode(@PathVariable String postCode) {
		return Response.success(sysPostService.getUsersByCode(postCode));
	}
}
