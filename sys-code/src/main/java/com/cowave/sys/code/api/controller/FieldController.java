/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.ModelField;
import com.cowave.sys.code.api.service.FieldService;
import com.cowave.sys.code.api.entity.TypeField;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 模型字段
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/field")
public class FieldController {

    private final FieldService fieldService;

    /**
     * Excel转换器
     */
    @GetMapping(value = "/converter/excel")
    public Response<List<Map<String, String>>> excelConverter() {
        return Response.success(fieldService.excelConverter());
    }

    /**
     * 字段类型
     */
    @GetMapping(value = "/types")
    public Response<List<TypeField>> types() {
        return Response.success(fieldService.types());
    }

    /**
     * 列表
     */
    @GetMapping(value = "/list/{modelId}")
    public Response<Response.Page<ModelField>> list(@PathVariable Long modelId) {
        return Response.page(fieldService.list(modelId));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<ModelField> info(@PathVariable Long id) {
        return Response.success(fieldService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody ModelField modelField) throws Exception {
        fieldService.add(modelField);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody ModelField modelField) throws Exception {
        fieldService.edit(modelField);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Integer[] id) throws Exception {
        fieldService.delete(id);
        return Response.success();
    }

    /**
     * 非空切换
     */
    @GetMapping(value = "/switch/notnull/{id}/{flag}")
    public Response<Void> switchNotnull(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchNotnull(id, flag);
        return Response.success();
    }

    /**
     * 是否集合字段切换
     */
    @GetMapping(value = "/switch/collect/{id}/{flag}")
    public Response<Void> switchCollect(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchCollect(id, flag);
        return Response.success();
    }

    /**
     * 是否Excel字段切换
     */
    @GetMapping(value = "/switch/excel/{id}/{flag}")
    public Response<Void> switchExcel(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchExcel(id, flag);
        return Response.success();
    }

    /**
     * 是否where条件切换
     */
    @GetMapping(value = "/switch/where/{id}/{flag}")
    public Response<Void> switchWhere(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchWhere(id, flag);
        return Response.success();
    }

    /**
     * 是否列表字段切换
     */
    @GetMapping(value = "/switch/list/{id}/{flag}")
    public Response<Void> switchList(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchList(id, flag);
        return Response.success();
    }

    /**
     * 是否详情字段切换
     */
    @GetMapping(value = "/switch/info/{id}/{flag}")
    public Response<Void> switchInfo(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchInfo(id, flag);
        return Response.success();
    }

    /**
     * 是否新增字段切换
     */
    @GetMapping(value = "/switch/insert/{id}/{flag}")
    public Response<Void> switchInsert(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchInsert(id, flag);
        return Response.success();
    }

    /**
     * 是否修改字段切换
     */
    @GetMapping(value = "/switch/edit/{id}/{flag}")
    public Response<Void> switchEdit(@PathVariable Long id, @PathVariable Integer flag) {
        fieldService.switchEdit(id, flag);
        return Response.success();
    }
}
