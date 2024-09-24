/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.controller;

import com.alibaba.excel.EasyExcel;
import com.cowave.commons.framework.support.excel.CellWidthHandler;
import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import com.cowave.sys.quartz.api.task.service.TaskService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 任务管理
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    /**
     * 重新加载
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:refresh')")
    @GetMapping("/refresh")
    public Response<Void> refresh() throws SchedulerException {
        taskService.refresh();
        return Response.success();
    }

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:query')")
    @GetMapping("/list")
    public Response<Response.Page<QuartzTask>> list(QuartzTask quartzTask) {
        return Response.page(taskService.list(quartzTask));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:query')")
    @GetMapping(value = "/info/{id}")
    public Response<QuartzTask> info(@PathVariable Long id) {
        return Response.success(taskService.info(id));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:new')")
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody QuartzTask quartzTask) throws SchedulerException {
        taskService.add(quartzTask);
        return Response.success();
    }

    /**
     * 修改
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:edit')")
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody QuartzTask quartzTask) throws SchedulerException {
        taskService.edit(quartzTask);
        return Response.success();
    }

    /**
     * 删除
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:delete')")
    @GetMapping("/delete")
    public Response<Void> delete(Long[] id) throws SchedulerException {
        taskService.delete(id);
        return Response.success();
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:status')")
    @GetMapping("/status/change")
    public Response<Void> changeStatus(@NotNull(message = "任务id不能为空") Long id,
                                       @NotNull(message = "任务状态不能为空") Integer status) throws SchedulerException {
        taskService.changeStatus(id, status);
        return Response.success();
    }

    /**
     * 立即执行
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:exec')")
    @GetMapping("/exec")
    public Response<Void> exec(@NotNull(message = "任务id不能为空") Long id) throws SchedulerException {
        taskService.exec(id);
        return Response.success();
    }

    /**
     * 导出
     */
    @PreAuthorize("@permit.hasPermit('monitor:quartz:export')")
    @RequestMapping("/export")
    public void export(HttpServletResponse response, QuartzTask quartzTask) throws IOException {
        String fileName = URLEncoder.encode("定时任务", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        EasyExcel.write(response.getOutputStream(), QuartzTask.class);
        PageHelper.clearPage();
        List<QuartzTask> list = taskService.list(quartzTask);
        EasyExcel.write(response.getOutputStream(), QuartzTask.class)
                .sheet("任务").registerWriteHandler(new CellWidthHandler()).doWrite(list);
    }
}
