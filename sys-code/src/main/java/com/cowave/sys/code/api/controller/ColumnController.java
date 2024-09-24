/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.TypeColumn;
import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.entity.DbTableColumn;
import com.cowave.sys.code.api.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库表字段
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/column")
public class ColumnController {

    private final ColumnService columnService;

    /**
     * 字段选项
     */
    @GetMapping(value = "/options/{modelId}")
    public Response<List<SelectOption>> options(@PathVariable Long modelId) {
        return Response.success(columnService.options(modelId));
    }

    /**
     * 字段类型
     */
    @GetMapping(value = "/types/{dbType}")
    public Response<List<String>> types(@PathVariable String dbType) {
        return Response.success(TypeColumn.dbTypes(dbType));
    }

    /**
     * 列表
     */
    @GetMapping(value = "/list/{tableId}")
    public Response<Response.Page<DbTableColumn>> list(@PathVariable Long tableId) {
        return Response.page(columnService.list(tableId));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<DbTableColumn> info(@PathVariable Long id) {
        return Response.success(columnService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody DbTableColumn tableColumn) throws Exception {
        columnService.add(tableColumn);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody DbTableColumn tableColumn) throws Exception {
        columnService.edit(tableColumn);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Integer[] id) throws Exception {
        columnService.delete(id);
        return Response.success();
    }

    /**
     * 非空切换
     */
    @GetMapping(value = "/switch/notnull/{id}/{flag}")
    public Response<Void> switchNotnull(@PathVariable Long id, @PathVariable Integer flag) {
        columnService.switchNotnull(id, flag);
        return Response.success();
    }

    /**
     * 主键切换
     */
    @GetMapping(value = "/switch/primary/{id}/{flag}")
    public Response<Void> switchPrimary(@PathVariable Long id, @PathVariable Integer flag) {
        columnService.switchPrimary(id, flag);
        return Response.success();
    }

    /**
     * 自增切换
     */
    @GetMapping(value = "/switch/increment/{id}/{flag}")
    public Response<Void> switchIncrement(@PathVariable Long id, @PathVariable Integer flag) {
        columnService.switchIncrement(id, flag);
        return Response.success();
    }
}
