/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.sys.admin.api.service.FlowPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.*;
import com.cowave.sys.admin.api.entity.flow.Purchase;

/**
 * 采购申请
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/purchase")
public class FlowPurchaseController {

    private final FlowPurchaseService flowPurchaseService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Purchase>> list(@RequestBody Purchase purchase) {
        return Response.page(flowPurchaseService.list(purchase));
    }

    /**
     * 详情
     */
    @GetMapping( "/info/{id}")
    public Response<Purchase> info(@PathVariable Long id) {
        return Response.success(flowPurchaseService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody Purchase purchase) {
        flowPurchaseService.add(purchase);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@RequestBody Purchase purchase) {
        flowPurchaseService.edit(purchase);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable Long[] ids) {
        flowPurchaseService.delete(ids);
        return Response.success();
    }
}
