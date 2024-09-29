/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.controller;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.meter.entity.ModelInfo;
import com.cowave.sys.meter.entity.SelectOption;
import com.cowave.sys.meter.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模型
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/model")
public class ModelController {

    private final ModelService modelService;

    /**
     * 模型选项
     */
    @GetMapping(value = "/options")
    public Response<List<SelectOption>> options() {
        return Response.success(modelService.options());
    }

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<ModelInfo>> list(@RequestBody ModelInfo modelInfo) {
        return Response.page(modelService.list(modelInfo));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<ModelInfo> info(@PathVariable Long id) {
        return Response.success(modelService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody ModelInfo modelInfo) throws Exception {
        modelService.add(modelInfo);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody ModelInfo modelInfo) throws Exception {
        modelService.edit(modelInfo);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Integer[] id) throws Exception {
        modelService.delete(id);
        return Response.success();
    }

    /**
     * 是否导出Excel切换
     */
    @GetMapping(value = "/switch/excel/{id}/{flag}")
    public Response<Void> switchExcel(@PathVariable Long id, @PathVariable Integer flag) {
        modelService.switchExcel(id, flag);
        return Response.success();
    }

    /**
     * 是否继承Access切换
     */
    @GetMapping(value = "/switch/access/{id}/{flag}")
    public Response<Void> switchAccess(@PathVariable Long id, @PathVariable Integer flag) {
        modelService.switchAccess(id, flag);
        return Response.success();
    }

    /**
     * 是否记录日志切换
     */
    @GetMapping(value = "/switch/log/{id}/{flag}")
    public Response<Void> switchLog(@PathVariable Long id, @PathVariable Integer flag) {
        modelService.switchLog(id, flag);
        return Response.success();
    }

    /**
     * 生成模型
     */
    @PostMapping(value = "/generate")
    public Response<Void> generate(@RequestBody ModelInfo modelInfo) {
        modelService.generate(modelInfo);
        return Response.success();
    }

    /**
     * 代码预览
     */
    @GetMapping("/preview/{id}")
    public Response<Map<String, String>> preview(@PathVariable Long id) {
        return Response.success(modelService.preview(id));
    }

    /**
     * 代码模板
     */
    @GetMapping("/template/{id}")
    public void template(HttpServletResponse response, @PathVariable Long id) throws IOException {
        byte[] data = modelService.template(id);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
