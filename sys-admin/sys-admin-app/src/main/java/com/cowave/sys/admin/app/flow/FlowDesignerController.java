/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.flow;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.service.flow.FlowDesignerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.flowable.validation.ValidationError;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 流程设计
 * @order 18
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
