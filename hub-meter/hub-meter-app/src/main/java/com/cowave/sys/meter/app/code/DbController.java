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
import com.cowave.sys.meter.domain.code.DbInfo;
import com.cowave.sys.meter.domain.code.SelectOption;
import com.cowave.sys.meter.service.code.DbService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
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
