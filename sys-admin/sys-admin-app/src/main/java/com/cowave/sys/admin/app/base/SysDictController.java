/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.base;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.domain.base.request.DictQuery;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import com.cowave.sys.admin.infra.base.SysDictHelper;
import com.cowave.sys.admin.service.base.SysDictService;
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
 * 字典
 * @order 7
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController {

	private final SysDictHelper sysDictHelper;

	private final SysDictService sysDictService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping
	public Response<List<DictInfoDto>> list(DictQuery query) {
		return Response.success(sysDictService.queryList(query));
	}

	/**
	 * 详情
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping("/{dictId}")
	public Response<DictInfoDto> info(@PathVariable Long dictId) {
		return Response.success(sysDictService.info(dictId));
	}

	/**
	 * 新增
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:create')")
	@PostMapping
	public Response<Void> create(@RequestBody DictInfoDto sysDict) throws Exception {
		return Response.success(() -> sysDictService.add(sysDict));
	}

	/**
	 * 删除
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:delete')")
	@DeleteMapping("/{dictIds}")
	public Response<Void> delete(@PathVariable List<Integer> dictIds) throws Exception {
		return Response.success(() -> sysDictService.delete(dictIds));
	}

	/**
	 * 修改
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:edit')")
	@PatchMapping
	public Response<Void> edit(@RequestBody DictInfoDto sysDict) throws Exception {
		return Response.success(() -> sysDictService.edit(sysDict));
	}

	/**
	 * 导出字典
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:export')")
	@PostMapping ("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), DictInfoDto.class)
		.sheet("字典数据").registerWriteHandler(new CellWidthHandler()).doWrite(sysDictService.queryList(new DictQuery()));
	}

	/**
	 * 刷新字典
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysDictHelper.refresh();
		return Response.success();
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/code/{dictCode}")
	public Response<DictInfoDto> getDict(@PathVariable String dictCode) {
		return Response.success(sysDictHelper.getDict(dictCode));
	}

	/**
	 * 获取类型字典
	 */
	@GetMapping("/type/{typeCode}")
	public Response<List<DictInfoDto>> getType(@PathVariable String typeCode) {
		return Response.success(sysDictHelper.getType(typeCode));
	}

	/**
	 * 获取分组字典
	 */
	@GetMapping("/group/{groupCode}")
	public Response<List<DictInfoDto>> getGroup(@PathVariable String groupCode) {
		return Response.success(sysDictHelper.getGroup(groupCode));
	}

	/**
	 * 获取分组类型
	 */
	@GetMapping("/group/types/{groupCode}")
	public Response<List<SelectOption>> groupOptions(@PathVariable String groupCode) {
		return Response.success(sysDictService.groupOptions(groupCode));
	}
}
