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
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.flow.Leave;
import com.cowave.sys.admin.service.flow.LeaveFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 请假申请
 * @order 23
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/leave")
public class LeaveFlowController {

    private final LeaveFlowService leaveFlowService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Leave>> list(@RequestBody Leave leave) {
        return Response.page(leaveFlowService.list(leave));
    }

    /**
     * 我的请假
     */
    @PostMapping("/list/my")
    public Response<Response.Page<Leave>> mylist(@RequestBody Leave leave) {
        leave.setApplyUser(Access.userId());
        return Response.page(leaveFlowService.list(leave));
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    public Response<Leave> info(@PathVariable Long id) {
        return Response.success(leaveFlowService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody Leave leave) {
        leaveFlowService.add(leave);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody Leave leave) {
        leaveFlowService.edit(leave);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable Long[] ids) {
        leaveFlowService.delete(ids);
        return Response.success();
    }

    /**
     * 撤销
     */
    @GetMapping( "/revocate/{id}")
    public Response<Void> revocate(@PathVariable Long id) {
        leaveFlowService.revocate(id);
        return Response.success();
    }
}
