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
import com.cowave.sys.meter.entity.AppInfo;
import com.cowave.sys.meter.service.AppService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 应用
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app")
public class AppController {

    private final AppService appService;

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<AppInfo>> list(@RequestBody AppInfo appInfo) {
        return Response.page(appService.list(appInfo));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/info/{id}")
    public Response<AppInfo> info(@PathVariable Long id) {
        return Response.success(appService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping(value = "/add")
    public Response<Void> add(@RequestBody AppInfo appInfo) throws Exception {
        appService.add(appInfo);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PostMapping(value = "/edit")
    public Response<Void> edit(@RequestBody AppInfo appInfo) throws Exception {
        appService.edit(appInfo);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping(value = "/delete")
    public Response<Void> delete(Integer[] id) throws Exception {
        appService.delete(id);
        return Response.success();
    }

    /**
     * 工程模板
     */
    @GetMapping("/template/{id}")
    public void template(HttpServletResponse response, @PathVariable Long id) throws IOException {
        byte[] data = appService.template(id);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
