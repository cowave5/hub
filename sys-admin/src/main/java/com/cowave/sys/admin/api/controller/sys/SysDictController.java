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

import com.cowave.sys.admin.api.caches.SysDictCaches;
import com.cowave.sys.admin.api.entity.SelectOption;
import com.cowave.sys.admin.api.service.SysDictService;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.sys.model.admin.SysDict;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;

/**
 * 字典
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController {

	private final SysDictCaches sysDictCaches;

	private final SysDictService sysDictService;

	/**
	 * 刷新缓存
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:cache')")
	@GetMapping("/refresh")
	public Response<Void> refresh() throws Exception {
		sysDictCaches.refresh();
		return Response.success();
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/cache/dict")
	public Response<SysDict> cacheDict(String dictCode) {
		return Response.success(sysDictCaches.getDictHelper(dictCode));
	}

	/**
	 * 获取类型字典
	 */
	@GetMapping("/cache/type")
	public Response<Response.Page<SysDict>> cacheType(String typeCode) {
		return Response.page(sysDictCaches.getType(typeCode));
	}

	/**
	 * 获取分组字典
	 */
	@GetMapping("/cache/group")
	public Response<Response.Page<SysDict>> cacheGroup(String groupCode) {
		return Response.page(sysDictCaches.getGroup(groupCode));
	}

	/**
	 * 字典类型选项
	 */
	@GetMapping("/options")
	public Response<List<SelectOption>> dictOptions() {
		return Response.success(sysDictService.dictOptions());
	}

	/**
	 * Group子类型选项
	 */
	@GetMapping("/group/options")
	public Response<List<SelectOption>> groupOptions(@NotNull(message = "{dict.notnull.groupcode}") String groupCode) {
		return Response.success(sysDictService.groupOptions(groupCode));
	}

	/**
	 * 列表
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping("/list")
	public Response<Response.Page<SysDict>> list(String groupCode, String typeCode) {
		return Response.page(sysDictService.list(groupCode, typeCode));
	}

	/**
	 * 详情
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:query')")
	@GetMapping(value = "/info/{dictId}")
	public Response<SysDict> info(@PathVariable Long dictId) {
		return Response.success(sysDictService.info(dictId));
	}

	/**
	 * 新增
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:new')")
	@PostMapping("/add")
	public Response<Void> add(@RequestBody SysDict sysDict) {
		sysDictService.add(sysDict);
		return Response.success();
	}

	/**
	 * 修改
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:edit')")
	@PostMapping("/edit")
	public Response<Void> edit(@RequestBody SysDict sysDict) {
		sysDictService.edit(sysDict);
		return Response.success();
	}

	/**
	 * 删除
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:delete')")
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{dict.notnull.id}") Long[] dictId) {
		sysDictService.delete(dictId);
		return Response.success();
	}

	/**
	 * 修改只读
	 */
	@PreAuthorize("@permit.hasPermit('sys:common:readonly')")
	@PostMapping("/change/readonly")
	public Response<Void> changeReadonly(@RequestBody SysDict sysDict) {
		sysDictService.changeReadonly(sysDict);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permit.hasPermit('sys:dict:export')")
	@RequestMapping ("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysDict.class)
		.sheet("字典数据").registerWriteHandler(new CellWidthHandler()).doWrite(sysDictService.list(null, null).getRecords());
	}
}
