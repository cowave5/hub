/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.controller;

import com.cowave.sys.code.api.entity.AppInfo;
import com.cowave.sys.code.api.service.AppService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.feign.codec.Response;
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
