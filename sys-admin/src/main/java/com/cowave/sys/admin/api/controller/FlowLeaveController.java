/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.api.entity.flow.Leave;
import com.cowave.sys.admin.api.service.FlowLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 请假申请
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/leave")
public class FlowLeaveController {

    private final FlowLeaveService flowLeaveService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Leave>> list(@RequestBody Leave leave) {
        return Response.page(flowLeaveService.list(leave));
    }

    /**
     * 我的请假
     */
    @PostMapping("/list/my")
    public Response<Response.Page<Leave>> mylist(@RequestBody Leave leave) {
        leave.setApplyUser(Access.userId());
        return Response.page(flowLeaveService.list(leave));
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    public Response<Leave> info(@PathVariable Long id) {
        return Response.success(flowLeaveService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody Leave leave) {
        flowLeaveService.add(leave);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody Leave leave) {
        flowLeaveService.edit(leave);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable Long[] ids) {
        flowLeaveService.delete(ids);
        return Response.success();
    }

    /**
     * 撤销
     */
    @GetMapping( "/revocate/{id}")
    public Response<Void> revocate(@PathVariable Long id) {
        flowLeaveService.revocate(id);
        return Response.success();
    }
}
