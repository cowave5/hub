/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.Satellite;
import com.cowave.sys.resource.api.entity.TreeNode;
import com.cowave.sys.resource.api.service.SatelliteService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 卫星
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/satellite")
public class SatelliteController {

    private final SatelliteService satelliteService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Valid @RequestBody Satellite satellite) {
        satelliteService.add(satellite);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Valid @RequestBody Satellite satellite) {
        satelliteService.edit(satellite);
        return Response.success();
    }

    /**
     * 删除
     * @param satId 卫星id
     */
    @GetMapping("/remove")
    public Response<Void> remove(@NotNull(message = "satId不能为空") Integer satId) {
        satelliteService.remove(satId);
        return Response.success();
    }

    /**
     * 详情
     * @param satId 卫星id
     */
    @GetMapping("/info")
    public Response<Satellite> info(@NotNull(message = "satId不能为空") Integer satId) {
        return Response.success(satelliteService.info(satId));
    }

    /**
     * 树
     */
    @GetMapping("/tree")
    public Response<List<TreeNode>> tree() {
        return Response.success(satelliteService.tree());
    }
}
