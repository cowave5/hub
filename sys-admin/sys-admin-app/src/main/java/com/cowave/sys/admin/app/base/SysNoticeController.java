/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.base;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.dto.*;
import com.cowave.sys.admin.domain.base.request.NoticeCreate;
import com.cowave.sys.admin.domain.base.request.NoticeMsgBack;
import com.cowave.sys.admin.domain.base.request.NoticeQuery;
import com.cowave.sys.admin.service.base.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通知公告
 * @order 17
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    /**
     * 列表
     */
    @GetMapping
    public Response<Response.Page<NoticeDto>> list(NoticeQuery query) {
        return Response.success(sysNoticeService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param noticeId 通知ID
     */
    @GetMapping("/{noticeId}")
    public Response<NoticeDto> info(@PathVariable Long noticeId) {
        return Response.success(sysNoticeService.info(Access.tenantId(), noticeId));
    }

    /**
     * 新增
     */
    @PostMapping
    public Response<Void> add(@Validated @RequestBody NoticeCreate sysNotice) throws Exception {
        return Response.success(() -> sysNoticeService.add(Access.tenantId(), sysNotice));
    }

    /**
     * 删除
     *
     * @param noticeIds 通知Id列表
     */
    @DeleteMapping("/{noticeIds}")
    public Response<Void> delete(@PathVariable List<Long> noticeIds) throws Exception {
        return Response.success(() -> sysNoticeService.delete(Access.tenantId(), noticeIds));
    }

    /**
     * 修改
     */
    @PatchMapping
    public Response<String> edit(@Validated @RequestBody NoticeCreate sysNotice) throws Exception {
        return Response.success(() -> sysNoticeService.edit(Access.tenantId(), sysNotice));
    }

    /**
     * 发布
     *
     * @param noticeId 通知Id
     */
    @PatchMapping("/publish/{noticeId}")
    public Response<Void> publish(@PathVariable Long noticeId) throws Exception {
        return Response.success(() -> sysNoticeService.publish(Access.tenantId(), noticeId));
    }

    /**
     * 已读情况
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/readers")
    public Response<Response.Page<NoticeUserDto>> getNoticeReaders(@NotNull(message = "{admin.notice.id.null}") Long noticeId) {
        return Response.success(sysNoticeService.getNoticeReaders(Access.tenantId(), noticeId));
    }

    /**
     * 消息列表
     */
    @GetMapping("/msg")
    public Response<Response.Page<NoticeMsgDto>> msgList() {
        return Response.page(sysNoticeService.msgList());
    }

    /**
     * 阅读消息
     *
     * @param noticeId 通知Id
     */
    @PatchMapping("/msg/read/{noticeId}")
    public Response<Void> msgRead(@PathVariable Long noticeId) throws Exception {
        return Response.success(() -> sysNoticeService.msgRead(noticeId));
    }

    /**
     * 反馈消息
     */
    @PostMapping("/msg/back")
    public Response<Void> msgBack(@Validated @RequestBody NoticeMsgBack msgBack) throws Exception {
        return Response.success(() -> sysNoticeService.msgBack(msgBack));
    }

    /**
     * 删除消息
     *
     * @param msgId 消息ID
     */
    @DeleteMapping("/msg/{msgId}")
    public Response<Void> msgDelete(@PathVariable Long msgId) throws Exception {
        return Response.success(() -> sysNoticeService.msgDelete(msgId));
    }

    /**
     * 未读消息计数
     */
    @GetMapping("/msg/unread")
    public Response<Long> msgUnReadCount() {
        return Response.success(sysNoticeService.msgUnReadCount(Access.userCode()));
    }
}
