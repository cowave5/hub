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
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.Operation;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
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

import static com.cowave.sys.admin.domain.base.constants.OpAction.*;
import static com.cowave.sys.admin.domain.base.constants.OpModule.SYSTEM;
import static com.cowave.sys.admin.domain.base.constants.OpModule.SYSTEM_POST;

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
	@PreAuthorize("@permits.hasPermit('sys:post:query')")
	@GetMapping
	public Response<Response.Page<SysPost>> list(DeptPostQuery query) {
		return Response.page(sysPostService.pageList(Access.tenantId(), query));
	}

	/**
	 * 详情
	 *
	 * @param postId 岗位id
	 */
	@PreAuthorize("@permits.hasPermit('sys:post:query')")
	@GetMapping("/{postId}")
	public Response<PostInfoDto> info(@PathVariable Integer postId) {
		return Response.success(sysPostService.info(Access.tenantId(), postId));
	}

	/**
	 * 新增
	 */
	@Operation(module = SYSTEM, type = SYSTEM_POST, action = CREATE, desc = "新增岗位：#{#sysPost.postName}")
	@PreAuthorize("@permits.hasPermit('sys:post:create')")
	@PostMapping
	public Response<Void> create(@Validated @RequestBody PostInfoDto sysPost) throws Exception {
		return Response.success(() -> sysPostService.create(Access.tenantId(), sysPost));
	}

	/**
	 * 删除
	 *
	 * @param postIds 岗位id列表
	 */
	@Operation(module = SYSTEM, type = SYSTEM_POST, action = DELETE, desc = "删除岗位")
	@PreAuthorize("@permits.hasPermit('sys:post:delete')")
	@DeleteMapping("/{postIds}")
	public Response<Void> delete(@PathVariable List<Integer> postIds) throws Exception {
		return Response.success(() -> sysPostService.delete(Access.tenantId(), postIds));
	}

	/**
	 * 修改
	 */
	@Operation(module = SYSTEM, type = SYSTEM_POST, action = EDIT, desc = "修改岗位：#{#sysPost.postName}")
	@PreAuthorize("@permits.hasPermit('sys:post:edit')")
	@PatchMapping
	public Response<Void> edit(@Validated @RequestBody PostInfoDto sysPost) throws Exception {
		return Response.success(() -> sysPostService.edit(Access.tenantId(), sysPost));
	}

	/**
	 * 导出岗位
	 */
	@PreAuthorize("@permits.hasPermit('sys:post:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response, DeptPostQuery query) throws IOException {
		String fileName = URLEncoder.encode("岗位数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		List<SysPost> postList = sysPostService.list(Access.tenantId(), query);
		EasyExcel.write(response.getOutputStream(), SysPost.class)
		.sheet("岗位").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(postList);
	}

	/**
	 * 岗位组织架构
	 */
	@PreAuthorize("@permits.hasPermit('sys:post:diagram')")
	@GetMapping("/diagram")
	public Response<Tree<Integer>> getDiagram() {
		return Response.success(sysPostService.getDiagram(Access.tenantId()));
	}

	/**
	 * 岗位流程候选人
	 *
	 * @param postCode 岗位编码
	 */
	@GetMapping("/candidates/{postCode}")
	public Response<List<UserNameDto>> getCandidatesByCode(@PathVariable String postCode) {
		return Response.success(sysPostService.getCandidatesByCode(Access.tenantId(), postCode));
	}

	/**
     * 岗位名称查询
     */
    @GetMapping("/name/{postId}")
    public Response<String> getNameById(@PathVariable Integer postId) {
        return Response.success(sysPostService.getNameById(Access.tenantId(), postId));
    }

	/**
     * 部门岗位名称查询
     */
    @GetMapping("/dept/name/{deptPosts}")
    public Response<List<String>> getNameOfDeptPost(@PathVariable List<String> deptPosts) {
        return Response.success(sysPostService.getNameOfDeptPost(Access.tenantId(), deptPosts));
    }
}
