/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.TaskUsage;
import com.cowave.sys.resource.api.service.TaskUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 任务资源管理
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task/usage")
public class TaskUsageController {

    private final TaskUsageService usageService;

    /**
     * 资源使用记录
     * @param taskId 任务id
     * @param latest 只取最新记录
     */
    @GetMapping("/list")
    public Response<Response.Page<ResourceUsage>> list(@NotNull(message = "taskId不能为空") String taskId, boolean latest) {
        return Response.page(usageService.list(taskId, latest));
    }

    /**
     * 历史使用记录
     * @param taskId 任务id
     */
    @GetMapping("/list/history")
    public Response<Response.Page<ResourceUsage>> historyList(@NotNull(message = "taskId不能为空") String taskId) {
        return Response.page(usageService.historyList(taskId));
    }

    /**
     * 申请资源
     */
    @PostMapping("/allocate")
    public Response<Response.Page<ResourceUsage>> allocate(@Valid @RequestBody TaskUsage taskUsage) {
        return Response.page(usageService.allocate(taskUsage));
    }

    /**
     * 自动申请
     */
    @PostMapping("/allocate/auto")
    public Response<Response.Page<ResourceUsage>> autoAllocate() {
        // TODO autoAllocate
        return Response.success();
    }

    /**
     * 释放资源
     * @param taskId 任务id
     */
    @GetMapping("/release")
    public Response<Void> release(@NotNull(message = "taskId不能为空") String taskId) {
        usageService.release(taskId);
        return Response.success();
    }

    /**
     * 调整资源
     */
    @PostMapping("/adjust")
    public Response<Response.Page<ResourceUsage>> adjust(@Valid @RequestBody TaskUsage taskUsage) {
        return Response.page(usageService.adjust(taskUsage));
    }
}
