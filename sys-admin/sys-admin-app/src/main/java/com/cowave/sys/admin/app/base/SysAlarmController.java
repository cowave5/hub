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
import com.cowave.commons.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.domain.base.dto.SysAlarmTypeDto;
import com.cowave.sys.admin.domain.base.vo.AlarmHandles;
import com.cowave.sys.admin.service.base.SysAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 系统告警
 * @order 13
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm")
public class SysAlarmController {

	private final SysAlarmService sysAlarmService;

	/**
     * 类型列表
     */
	@PostMapping(value = "/type/list")
    public Response<Response.Page<SysAlarmTypeDto>> typeList(@RequestBody SysAlarmTypeDto sysAlarmType) {
        return Response.page(sysAlarmService.typeList(sysAlarmType));
    }

	/**
     * 类型新增
     */
	@PostMapping(value = "/type/add")
    public Response<Void> typeAdd(@Validated @RequestBody SysAlarmTypeDto sysAlarmType) {
		sysAlarmService.typeAdd(sysAlarmType);
        return Response.success();
    }

    /**
     * 类型修改
     */
	@PostMapping(value = "/type/edit")
    public Response<Void> typeEdit(@Validated @RequestBody SysAlarmTypeDto sysAlarmType) {
		sysAlarmService.typeEdit(sysAlarmType);
        return Response.success();
    }

    /**
     * 类型删除
     */
	@GetMapping(value = "/type/delete")
    public Response<Void> typeDelete(Long id) {
		sysAlarmService.typeDelete(id);
        return Response.success();
    }

	/**
	 * 列表
	 */
	@PostMapping("/list")
    public Response<Response.Page<SysAlarmDto>> list(@RequestBody SysAlarmDto sysAlarm) {
        return Response.page(sysAlarmService.list(sysAlarm));
    }

	/**
	 * 详情
	 *
	 * @param id 告警id
	 */
	@GetMapping(value = "/info")
    public Response<SysAlarmDto> info(@NotNull(message = "{admin.alarm.id.notnull}") Long id) {
        return Response.success(sysAlarmService.info(id));
    }

	/**
	 * 删除
	 *
	 * @param id 告警id
	 */
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{admin.alarm.id.notnull}") Long id) {
		sysAlarmService.delete(id);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PostMapping("/export")
	public void export(HttpServletResponse response, SysAlarmDto sysAlarm) throws IOException {
		String fileName = URLEncoder.encode("系统告警", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), SysAlarmDto.class)
		.sheet("系统告警").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(sysAlarmService.list(sysAlarm).getRecords());
	}

	/**
     * 告警处理
     */
    @PostMapping("/handle")
    public Response<Void> handle(@Validated @RequestBody AlarmHandles alarmHandles) {
        sysAlarmService.handle(alarmHandles);
        return Response.success();
    }
}
