/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.Tunnel;
import com.cowave.sys.resource.api.service.TunnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 通道
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tunnel")
public class TunnelController {

    private final TunnelService tunnelService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Valid @RequestBody Tunnel tunnel) {
        tunnelService.add(tunnel);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Valid @RequestBody Tunnel tunnel) {
        tunnelService.edit(tunnel);
        return Response.success();
    }

    /**
     * 删除
     * @param tunnelId 通道id
     */
    @GetMapping("/remove")
    public Response<Void> remove(@NotNull(message = "tunnelId不能为空") Integer tunnelId) {
        tunnelService.remove(tunnelId);
        return Response.success();
    }

    /**
     * 详情
     * @param tunnelId 通道id
     */
    @GetMapping("/info")
    public Response<Tunnel> info(@NotNull(message = "tunnelId不能为空") Integer tunnelId) {
        return Response.success(tunnelService.info(tunnelId));
    }
}
