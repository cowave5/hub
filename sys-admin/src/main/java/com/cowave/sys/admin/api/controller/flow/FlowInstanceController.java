/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.flow;

import com.cowave.sys.admin.api.entity.flow.FlowInstance;
import com.cowave.sys.admin.api.entity.flow.FlowTask;
import com.cowave.sys.admin.api.entity.flow.FlowVariable;
import com.cowave.sys.admin.api.service.flow.FlowInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * 流程实例
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/instance")
public class FlowInstanceController {

    private final FlowInstanceService flowInstanceService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<FlowInstance>> list(@RequestBody FlowInstance flowInstance) {
        return Response.success(flowInstanceService.list(flowInstance));
    }

    /**
     * 流程进度
     */
    @GetMapping({"/progress/{id}"})
    public Response<String> progress(@PathVariable("id") String id) throws IOException {
        return Response.success(flowInstanceService.progress(id));
    }

    /**
     * 流程记录
     */
    @PostMapping("/history")
    public Response<Response.Page<FlowTask>> history(@RequestBody FlowTask flowTask) {
        return Response.success(flowInstanceService.history(flowTask.getProcessInstanceId()));
    }

    /**
     * 流程变量
     */
    @PostMapping("/variables")
    public Response<Response.Page<FlowVariable>> variables(@RequestBody FlowTask flowTask) {
        return Response.success(flowInstanceService.variables(flowTask.getProcessInstanceId()));
    }

    /**
     * 执行实例
     */
    @PostMapping("/executions")
    public Response<List<FlowInstance>> executions(String name) {
        return Response.success(flowInstanceService.executions(name));
    }

    /**
     * 流程挂起
     */
    @GetMapping("/suspend/{id}")
    public Response<Void> suspend(@PathVariable String id) {
        flowInstanceService.suspend(id);
        return Response.success();
    }

    /**
     * 流程唤醒
     */
    @GetMapping("/activate/{id}")
    public Response<Void> activate(@PathVariable String id) {
        flowInstanceService.activate(id);
        return Response.success();
    }

    /**
     * 跳转流程
     */
    @GetMapping("/jump/{taskId}/{tid}")
    public Response<Void> jump(@PathVariable String taskId, @PathVariable String tid, String comment) {
        flowInstanceService.jump(taskId, tid, comment);
        return Response.success();
    }

    /**
     * 修改流程变量
     */
    @PostMapping("/variables/edit")
    public Response<Void> variablesEdit(@RequestBody FlowVariable flowVariable) {
        flowInstanceService.variablesEdit(flowVariable);
        return Response.success();
    }

    /**
     * 删除流程变量
     */
    @PostMapping("/variables/delete")
    public Response<Void> variablesDelete(@RequestBody FlowVariable flowVariable) {
        flowInstanceService.variablesDelete(flowVariable);
        return Response.success();
    }

    /**
     * 流程删除
     */
    @GetMapping("/delete/{ids}")
    public Response<Void> delete(@PathVariable String[] ids) {
        flowInstanceService.delete(ids);
        return Response.success();
    }
}
