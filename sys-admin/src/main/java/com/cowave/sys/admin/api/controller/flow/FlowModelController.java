/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.flow;

import com.cowave.sys.admin.api.service.flow.FlowModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cowave.sys.admin.api.entity.flow.FlowModel;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.repository.Model;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 流程模型
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/model")
public class FlowModelController {

    private final FlowModelService flowModelService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Model>> list(@RequestBody FlowModel flowModel) {
        return Response.success(flowModelService.list(flowModel));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody FlowModel flowModel) throws JsonProcessingException {
        flowModelService.add(flowModel);
        return Response.success();
    }

    /**
     * 发布
     * @param modelId 流程id
     */
    @GetMapping("/deploy/{modelId}")
    public Response<Void> publish(@PathVariable String modelId) throws IOException {
        flowModelService.publish(modelId);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping("/delete/{modelIds}")
    public Response<Void> delete(@PathVariable String[] modelIds) {
        flowModelService.delete(modelIds);
        return Response.success();
    }

    /**
     * 导出
     */
    @RequestMapping("/export/{modelId}")
    public void export(@PathVariable String modelId, HttpServletResponse response) throws IOException {
        flowModelService.export(modelId, response);
    }
}
