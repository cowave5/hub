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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.cowave.sys.admin.api.entity.LogQuery;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.model.admin.SysLog;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.support.excel.CellWidthHandler;

import lombok.RequiredArgsConstructor;

/**
 * 操作日志
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/log")
public class SysLogController {

	private final SysLogService sysLogService;

	/**
	 * 列表
	 */
	@PostMapping("/list")
    public Response<Response.Page<SysLog>> list(@RequestBody SysLog sysLog) {
        return Response.page(sysLogService.list(sysLog));
    }

	/**
	 * 详情
	 *
	 * @param id 日志id
	 */
	@GetMapping(value = "/info/{id}")
    public Response<SysLog> info(@PathVariable Long id) {
        return Response.success(sysLogService.info(id));
    }

	/**
	 * 删除
	 *
	 * @param id 日志id
	 */
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{valid.notnull.oper.id}") Long[] id) {
		sysLogService.delete(id);
		return Response.success();
	}

	/**
	 * 清空
	 */
	@GetMapping("/clean")
	public Response<Void> clean() {
		sysLogService.clean();
		return Response.success();
	}

	/**
	 * 导出
	 */
	@RequestMapping("/export")
	public void export(HttpServletResponse response, SysLog sysLog) throws IOException {
		String fileName = URLEncoder.encode("操作日志", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysLog.class)
		.sheet("操作日志").registerWriteHandler(new CellWidthHandler()).doWrite(sysLogService.list(sysLog).getRecords());
	}

	/**
	 * 用户日志信息查询
	 */
	@PostMapping("/user/query")
	public Response<LogQuery> userLogQuery(@RequestBody LogQuery logQuery) {
		return Response.success(sysLogService.userLogQuery(logQuery));
	}

	/**
	 * 部门日志信息查询
	 */
	@PostMapping("/dept/query")
	public Response<LogQuery> deptLogQuery(@RequestBody LogQuery logQuery) {
		return Response.success(sysLogService.deptLogQuery(logQuery));
	}

	/**
	 * 岗位日志信息查询
	 */
	@PostMapping("/post/query")
	public Response<LogQuery> postLogQuery(@RequestBody LogQuery logQuery) {
		return Response.success(sysLogService.postLogQuery(logQuery));
	}
}
