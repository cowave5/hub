/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 资源使用管理
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

    private final UsageService usageService;

    /**
     * 使用记录
     * @param usageId 使用id
     */
    @GetMapping("/list")
    public Response<Response.Page<ResourceUsage>> list(@NotNull(message = "usageId不能为空") Long usageId) {
        return Response.page(usageService.list(usageId));
    }

    /**
     * 冲突记录
     */
    @PostMapping("/list/conflict")
    public Response<Response.Page<ResourceUsage>> conflictList(@Valid @RequestBody Usage usage) {
        return Response.page(usageService.conflictList(usage));
    }
}
