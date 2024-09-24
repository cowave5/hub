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
import com.cowave.sys.resource.api.entity.TreeNode;
import com.cowave.sys.resource.api.service.PoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * 资源池管理
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pool")
public class PoolController {

    private final PoolService poolService;

    /**
     * 树
     */
    @GetMapping("/tree")
    public Response<Collection<TreeNode>> tree() {
        return Response.success(poolService.tree());
    }

    /**
     * 创建
     */
    @PostMapping("/create")
    public Response<Void> create(@RequestBody ResourcePool pool) {
        poolService.create(pool);
        return Response.success();
    }

    /**
     * 删除
     * @param srcId 资源id
     */
    @GetMapping("/remove")
    public Response<Void> remove(@NotNull(message = "srcId不能为空") Long srcId) {
        poolService.remove(srcId);
        return Response.success();
    }
}
