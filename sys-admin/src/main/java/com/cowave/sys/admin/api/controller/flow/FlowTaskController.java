/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.flow;

import com.cowave.sys.admin.api.entity.flow.FlowTask;
import com.cowave.sys.admin.api.entity.flow.TaskComplete;
import com.cowave.sys.admin.api.service.flow.FlowTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程任务
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/task")
public class FlowTaskController {

    private final FlowTaskService flowTaskService;

    /**
     * 全部待办
     */
    @PostMapping("/list/all")
    public Response<Response.Page<FlowTask>> listAll(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.listAll(flowTask));
    }

    /**
     * 我的待办
     */
    @PostMapping("/list")
    public Response<Response.Page<FlowTask>> list(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.list(flowTask));
    }

    /**
     * 我办理的
     */
    @PostMapping("/list/history")
    public Response<Response.Page<FlowTask>> listHistory(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.listHistory(flowTask));
    }

    /**
     * 任务表单
     */
    @PostMapping("/form/{taskId}")
    public Response<String> form(@PathVariable String taskId) {
        return Response.success(flowTaskService.form(taskId));
    }

    /**
     * 任务办理
     */
    @PostMapping("/complete")
    public Response<Void> complete(@Validated @RequestBody TaskComplete taskComplete) {
        flowTaskService.complete(taskComplete);
        return Response.success();
    }

    /**
     * 办理过程
     */
    @GetMapping("/records/{taskId}")
    public Response<List<FlowTask>> records(@PathVariable String taskId) {
        return Response.success(flowTaskService.records(taskId));
    }

    /**
     * 修改办理人
     */
    @GetMapping("/assignee/{taskId}/{userId}")
    public Response<Void> assignee(@PathVariable String taskId, @PathVariable String userId) {
        flowTaskService.assignee(taskId, userId);
        return Response.success();
    }

    /**
     * 催办
     */
    @GetMapping("/press/{taskId}")
    public Response<Void> press(@PathVariable String taskId) {
        flowTaskService.press(taskId);
        return Response.success();
    }
}
