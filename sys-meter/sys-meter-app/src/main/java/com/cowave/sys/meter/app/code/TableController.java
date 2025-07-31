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
import com.cowave.sys.meter.domain.code.DbTable;
import com.cowave.sys.meter.domain.code.SelectOption;
import com.cowave.sys.meter.service.code.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据库表
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/table")
public class TableController {

    private final TableService tableService;

    /**
     * 表选项
     */
    @GetMapping(value = "/options/{appId}")
    public Response<List<SelectOption>> options(@PathVariable Long appId) {
        return Response.success(tableService.options(appId));
    }

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<DbTable>> list(@RequestBody DbTable dbTable) {
        return Response.page(tableService.list(dbTable));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<DbTable> info(@PathVariable Long id) {
        return Response.success(tableService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody DbTable dbTable) throws Exception {
        tableService.add(dbTable);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody DbTable dbTable) throws Exception {
        tableService.edit(dbTable);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Integer[] id) throws Exception {
        tableService.delete(id);
        return Response.success();
    }

    /**
     * DDL预览
     */
    @GetMapping("/preview/{id}")
    public Response<Map<String, String>> preview(@PathVariable Long id) {
        return Response.success(tableService.preview(id));
    }
}
