/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.DbInfo;
import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.service.DbService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据库
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/db")
public class DbController {

    private final DbService dbService;

    /**
     * 数据库选项
     */
    @GetMapping(value = "/options")
    public Response<List<SelectOption>> options(Long projectId) {
        return Response.success(dbService.options(projectId));
    }

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<DbInfo>> list(@RequestBody DbInfo dbInfo) {
        return Response.page(dbService.list(dbInfo));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<DbInfo> info(@PathVariable Long id) {
        return Response.success(dbService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody DbInfo dbInfo) throws Exception {
        dbService.add(dbInfo);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody DbInfo dbInfo) throws Exception {
        dbService.edit(dbInfo);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Long[] id) throws Exception {
        dbService.delete(id);
        return Response.success();
    }

    /**
     * 从数据库同步表信息
     */
    @PostMapping(value = "/synTable")
    public Response<Void> synTable(@RequestBody DbInfo dbInfo) throws Exception {
        dbService.synTable(dbInfo);
        return Response.success();
    }

    /**
     * DDL预览
     */
    @GetMapping("/preview/{id}")
    public Response<Map<String, String>> preview(@PathVariable Long id) {
        return Response.success(dbService.preview(id));
    }

    /**
     * DDL模板
     */
    @GetMapping("/template/{id}")
    public void template(HttpServletResponse response, @PathVariable Long id) throws IOException {
        byte[] data = dbService.template(id);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
