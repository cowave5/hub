/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.sys;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.sys.HubDict;
import com.cowave.hub.admin.domain.sys.dto.DictInfoDto;
import com.cowave.hub.admin.domain.sys.request.DictQuery;
import com.cowave.hub.admin.domain.sys.vo.SelectOption;
import com.cowave.hub.admin.service.sys.HubDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
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
public class HubDictController {

	private final HubDictService hubDictService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:query')")
	@GetMapping
	public Response<List<DictInfoDto>> list(DictQuery query) {
		return Response.success(hubDictService.queryList(query));
	}

	/**
	 * 详情
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:query')")
	@GetMapping("/{dictId}")
	public Response<DictInfoDto> info(@PathVariable Long dictId) {
		return Response.success(hubDictService.info(dictId));
	}

	/**
	 * 新增
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:create')")
	@PostMapping
	public Response<Void> create(@RequestBody DictInfoDto dict) throws Exception {
		return Response.success(() -> hubDictService.add(dict));
	}

	/**
	 * 删除
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:delete')")
	@DeleteMapping("/{dictIds}")
	public Response<Void> delete(@PathVariable List<Integer> dictIds) throws Exception {
		return Response.success(() -> hubDictService.delete(dictIds));
	}

	/**
	 * 修改
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:edit')")
	@PatchMapping
	public Response<Void> edit(@RequestBody DictInfoDto dict) throws Exception {
		return Response.success(() -> hubDictService.edit(dict));
	}

	/**
	 * 导出字典
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:export')")
	@PostMapping ("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), DictInfoDto.class)
		.sheet("字典数据").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(hubDictService.queryList(new DictQuery()));
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/code/{dictCode}")
	public Response<HubDict> getByCode(@PathVariable String dictCode) {
		return Response.success(hubDictService.getByCode(dictCode));
	}

	/**
	 * 获取类型字典
	 */
	@GetMapping("/type/{typeCode}")
	public Response<List<HubDict>> listByType(@PathVariable String typeCode) {
		return Response.success(hubDictService.listByType(typeCode));
	}

	/**
	 * 获取分组字典
	 */
	@GetMapping("/group/{groupCode}")
	public Response<List<HubDict>> listByGroup(@PathVariable String groupCode) {
		return Response.success(hubDictService.listByGroup(groupCode));
	}

	/**
	 * 获取分组类型
	 */
	@GetMapping("/group/types/{groupCode}")
	public Response<Collection<SelectOption>> listTypeByGroup(@PathVariable String groupCode) {
		return Response.success(hubDictService.listTypeByGroup(groupCode));
	}
}
