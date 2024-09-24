/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.flow;

import com.cowave.sys.admin.api.service.flow.FlowDeployService;
import lombok.RequiredArgsConstructor;

import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cowave.sys.admin.api.entity.flow.FlowDeploy;

import java.io.IOException;
import java.util.*;

/**
 * 流程部署
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/deploy")
public class FlowDeployController {

    private final FlowDeployService flowDeployService;

    /**
     * 流程选项
     */
    @GetMapping(value = "/options")
    public Response<List<FlowDeploy>> options() {
        return Response.success(flowDeployService.options());
    }

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<FlowDeploy>> list(@Validated @RequestBody FlowDeploy flowDeploy) {
        return Response.success(flowDeployService.list(flowDeploy));
    }

    /**
     * 部署
     */
    @RequestMapping(value = "/upload")
    public Response<Void> upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        assert filename != null;
        flowDeployService.upload(file, filename);
        return Response.success(null, "导入成功: " + filename);
    }

    /**
     * 流程定义
     */
    @GetMapping("/definition")
    public Response<Map<String, String>> definition(String deploymentId, String resourceName) throws Exception {
        return Response.success(flowDeployService.definition(deploymentId, resourceName));
    }

    /**
     * 流程图
     */
    @GetMapping("/diagram/{id}")
    public Response<String> diagram(@PathVariable("id") String id) throws Exception {
        return Response.success(flowDeployService.diagram(id));
    }

    /**
     * 转为模型
     */
    @GetMapping("/translate/{id}")
    public Response<Void> translate(@PathVariable("id") String id) {
        String definitionName = flowDeployService.translate(id);
        return Response.success(null, "[" + definitionName + "]流程转为模型成功");
    }

    /**
     * 删除部署
     */
    @GetMapping("/delete/{deploymentIds}")
    public Response<Void> delete(@PathVariable String[] deploymentIds) {
        flowDeployService.delete(deploymentIds);
        return Response.success();
    }
}
