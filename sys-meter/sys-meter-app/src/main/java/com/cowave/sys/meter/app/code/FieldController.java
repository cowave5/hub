/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.app.code;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.meter.domain.code.ModelField;
import com.cowave.sys.meter.domain.code.TypeField;
import com.cowave.sys.meter.service.code.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
