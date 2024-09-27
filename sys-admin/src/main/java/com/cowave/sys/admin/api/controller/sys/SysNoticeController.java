/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller.sys;

import javax.validation.constraints.NotNull;

import com.cowave.sys.admin.api.service.SysNoticeService;
import com.cowave.sys.model.admin.SysAttach;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公告
 *
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
    @PostMapping("/list")
    public Response<Response.Page<SysNotice>> list(@RequestBody SysNotice sysNotice) {
        return Response.page(sysNoticeService.list(sysNotice));
    }

    /**
     * 详情
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/info/{noticeId}")
    public Response<SysNotice> info(@PathVariable Long noticeId) {
        return Response.success(sysNoticeService.info(noticeId));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody SysNotice sysNotice) throws Exception {
        sysNoticeService.add(sysNotice);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<String> edit(@Validated @RequestBody SysNotice sysNotice) throws Exception {
        sysNoticeService.edit(sysNotice);
        return Response.success();
    }

    /**
     * 图片上传
     */
    @PostMapping("/image")
    public Response<SysAttach> imageUpload(@RequestParam("file") MultipartFile file, SysAttach image) throws Exception {
        return Response.success(sysNoticeService.imageUpload(file, image));
    }

    /**
     * 删除
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/delete")
    public Response<Void> delete(@NotNull(message = "{notice.notnull.id}") Long[] noticeId) throws Exception {
        sysNoticeService.delete(noticeId);
        return Response.success();
    }

    /**
     * 发布
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/publish/{noticeId}")
    public Response<Void> publish(@PathVariable Long noticeId) {
        sysNoticeService.publish(noticeId);
        return Response.success();
    }

    /**
     * 已读列表
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/read/list")
    public Response<Response.Page<SysNoticeRead>> readList(@NotNull(message = "{notice.notnull.id}") Long noticeId) {
        return Response.page(sysNoticeService.readList(noticeId));
    }

    /**
     * 消息列表
     */
    @GetMapping("/msg/list")
    public Response<Response.Page<SysNotice>> msgList() {
        return Response.page(sysNoticeService.msgList());
    }

    /**
     * 阅读消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/msg/read/{noticeId}")
    public Response<Void> msgRead(@PathVariable Long noticeId) {
        sysNoticeService.msgRead(noticeId);
        return Response.success();
    }

    /**
     * 反馈消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping(value = "/msg/back")
    public Response<Void> msgBack(@NotNull(message = "{notice.notnull.id}") Long noticeId, String readBack) {
        sysNoticeService.msgBack(noticeId, readBack);
        return Response.success();
    }

    /**
     * 删除消息
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/msg/delete")
    public Response<Void> msgDelete(@NotNull(message = "{notice.notnull.id}") Long noticeId) {
        sysNoticeService.msgDelete(noticeId);
        return Response.success();
    }
}
