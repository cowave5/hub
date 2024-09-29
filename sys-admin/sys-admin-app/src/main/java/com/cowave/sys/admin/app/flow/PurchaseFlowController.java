/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.flow;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.flow.Purchase;
import com.cowave.sys.admin.service.flow.PurchaseFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 采购申请
 * @order 23
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/purchase")
public class PurchaseFlowController {

    private final PurchaseFlowService purchaseFlowService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Purchase>> list(@RequestBody Purchase purchase) {
        return Response.page(purchaseFlowService.list(purchase));
    }

    /**
     * 详情
     */
    @GetMapping( "/info/{id}")
    public Response<Purchase> info(@PathVariable Long id) {
        return Response.success(purchaseFlowService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody Purchase purchase) {
        purchaseFlowService.add(purchase);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@RequestBody Purchase purchase) {
        purchaseFlowService.edit(purchase);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable Long[] ids) {
        purchaseFlowService.delete(ids);
        return Response.success();
    }
}
