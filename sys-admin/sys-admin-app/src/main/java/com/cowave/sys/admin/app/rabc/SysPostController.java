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

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.domain.rabc.SysPost;
import com.cowave.sys.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.sys.admin.service.rabc.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 岗位
 * @order 2
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class SysPostController {

	private final SysPostService sysPostService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@GetMapping
	public Response<Response.Page<SysPost>> list(DeptPostQuery query) {
		return Response.page(sysPostService.pageList(query));
	}

	/**
	 * 详情
	 *
	 * @param postId 岗位id
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:query')")
	@GetMapping("/{postId}")
	public Response<PostInfoDto> info(@PathVariable Integer postId) {
		return Response.success(sysPostService.info(postId));
	}

	/**
	 * 新增
	 */
	@Operation(module = "op_admin", type = "op_post", action = "op_create", desc = "新增岗位：#{#sysPost.postName}")
	@PreAuthorize("@permit.hasPermit('sys:post:create')")
	@PostMapping
	public Response<Void> add(@Validated @RequestBody PostInfoDto sysPost) throws Exception {
		return Response.success(() -> sysPostService.add(sysPost));
	}

	/**
	 * 删除
	 *
	 * @param postIds 岗位id列表
	 */
	@Operation(module = "op_admin", type = "op_post", action = "op_delete", desc = "删除岗位")
	@PreAuthorize("@permit.hasPermit('sys:post:delete')")
	@DeleteMapping("/{postIds}")
	public Response<Void> delete(@PathVariable List<Integer> postIds) throws Exception {
		return Response.success(() -> sysPostService.delete(postIds));
	}

	/**
	 * 修改
	 */
	@Operation(module = "op_admin", type = "op_post", action = "op_edit", desc = "修改岗位：#{#sysPost.postName}")
	@PreAuthorize("@permit.hasPermit('sys:post:edit')")
	@PatchMapping
	public Response<Void> edit(@Validated @RequestBody PostInfoDto sysPost) throws Exception {
		return Response.success(() -> sysPostService.edit(sysPost));
	}

	/**
	 * 导出岗位
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response, DeptPostQuery query) throws IOException {
		String fileName = URLEncoder.encode("岗位数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysPost.class)
		.sheet("岗位").registerWriteHandler(new CellWidthHandler()).doWrite(sysPostService.list(query));
	}

	/**
	 * 岗位组织架构
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:diagram')")
	@GetMapping("/diagram")
	public Response<Tree<String>> getDiagram() {
		return Response.success(sysPostService.getDiagram());
	}

	/**
	 * 刷新岗位组织
	 */
	@PreAuthorize("@permit.hasPermit('sys:post:cache')")
	@GetMapping("/diagram/refresh")
	public Response<Void> refreshDiagram() throws Exception {
		return Response.success(sysPostService::refreshDiagram);
	}

	/**
	 * 岗位流程候选人
	 *
	 * @param postCode 岗位编码
	 */
	@GetMapping("/candidates/{postCode}")
	public Response<List<UserNameDto>> getCandidatesByCode(@PathVariable String postCode) {
		return Response.success(sysPostService.getCandidatesByCode(postCode));
	}

	/**
     * 岗位名称查询
     */
    @GetMapping("/name/{postId}")
    public Response<String> getNameById(@PathVariable Integer postId) {
        return Response.success(sysPostService.getNameById(postId));
    }

	/**
     * 部门岗位名称查询
     */
    @GetMapping("/dept/name/{deptPosts}")
    public Response<List<String>> getNameOfDeptPost(@PathVariable List<String> deptPosts) {
        return Response.success(sysPostService.getNameOfDeptPost(deptPosts));
    }
}
