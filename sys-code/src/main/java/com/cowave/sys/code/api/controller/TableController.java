/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.entity.DbTable;
import com.cowave.sys.code.api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
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
