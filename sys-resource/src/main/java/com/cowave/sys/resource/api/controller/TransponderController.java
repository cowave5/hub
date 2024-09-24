/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.model.resource.Transponder;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.service.TransponderService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 转发器
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transponder")
public class TransponderController {

    private final TransponderService tspdService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Valid @RequestBody Transponder transponder) {
        tspdService.add(transponder);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Valid @RequestBody Transponder transponder) {
        tspdService.edit(transponder);
        return Response.success();
    }

    /**
     * 删除
     * @param tspdId 转发器id
     */
    @GetMapping("/remove")
    public Response<Void> remove(@NotNull(message = "tspdId不能为空") Integer tspdId) {
        tspdService.remove(tspdId);
        return Response.success();
    }

    /**
     * 详情
     * @param tspdId 转发器id
     */
    @GetMapping("/info")
    public Response<Transponder> info(@NotNull(message = "tspdId不能为空") Integer tspdId) {
        return Response.success(tspdService.info(tspdId));
    }

    /**
     * 卫星转发器
     * @param satId 卫星id
     */
    @GetMapping("/satellite")
    public Response<Response.Page<Transponder>> satellite(@NotNull(message = "satId不能为空") Integer satId) {
        return Response.page(tspdService.satelliteQuery(satId));
    }

    /**
     * 波束转发器
     * @param beamId 波束id
     */
    @GetMapping("/beam")
    public Response<Response.Page<Transponder>> beam(@NotNull(message = "beamId不能为空") Integer beamId) {
        return Response.page(tspdService.beamQuery(beamId));
    }

    /**
     * 通道转发器
     * @param tunnelId 通道id
     */
    @GetMapping("/tunnel")
    public Response<Response.Page<Transponder>> tunnel(@NotNull(message = "tunnelId不能为空") Integer tunnelId) {
        return Response.page(tspdService.tunnelQuery(tunnelId));
    }

    /**
     * 转发器资源记录
     * @param tspdId 转发器id
     */
    @GetMapping("/pool/list")
    public Response<Response.Page<ResourcePool>> poolList(@NotNull(message = "tspdId不能为空") Integer tspdId) {
        return Response.page(tspdService.poolList(tspdId));
    }

    /**
     * 转发器使用记录
     * @param tspdId 转发器id
     */
    @GetMapping("/usage/list")
    public Response<Response.Page<ResourceUsage>> usageList(@NotNull(message = "tspdId不能为空") Integer tspdId) {
        return Response.page(tspdService.usageList(tspdId));
    }

    /**
     * 转发器指定时间内使用记录
     */
    @PostMapping("/time/usage/list")
    public Response<Response.Page<ResourceUsage>> timeUsageList(Usage usage) {
        return Response.page(tspdService.timeUsageList(usage));
    }
}
