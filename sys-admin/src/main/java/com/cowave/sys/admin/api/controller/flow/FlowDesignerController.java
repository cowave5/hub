/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.flow;

import com.cowave.sys.admin.api.service.flow.FlowDesignerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.flowable.validation.ValidationError;
import org.springframework.feign.codec.Response;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.util.*;

/**
 * 流程设计
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/designer")
public class FlowDesignerController {

    private final FlowDesignerService flowDesignerService;

    /**
     * 账户信息
     */
    @GetMapping("/account")
    public Response<Void> account() {
        return Response.success();
    }

    /**
     * 流程信息
     */
    @GetMapping("/info/{modelId}")
    public ObjectNode info(@PathVariable String modelId) throws IOException {
        return flowDesignerService.info(modelId);
    }

    /**
     * 保存
     */
    @PostMapping("save/{modelId}")
    public void save(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) throws Exception {
        flowDesignerService.save(modelId, values);
    }

    /**
     * 校验
     */
    @PostMapping("/validate")
    public List<ValidationError> validate(@RequestBody JsonNode body) {
        return flowDesignerService.validate(body);
    }

    /**
     * 流程创建
     */
    @GetMapping("/create/{flowKey}")
    public Response<Void> create(@PathVariable String flowKey) throws IOException {
        flowDesignerService.create(flowKey);
        return Response.success();
    }
}
