/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.ModelInfo;
import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.feign.codec.Response;
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
