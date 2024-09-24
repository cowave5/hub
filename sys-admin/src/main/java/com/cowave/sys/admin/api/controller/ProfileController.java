/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.sys.admin.api.entity.UserProfile;
import com.cowave.sys.admin.api.service.ProfileService;
import com.cowave.sys.admin.api.service.SysAttachService;
import com.cowave.sys.model.admin.SysAttach;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final SysAttachService sysAttachService;

    private final ProfileService profileService;

    /**
     * 详情
     */
    @GetMapping(value = {"/info"})
    public Response<UserProfile> info() {
        return Response.success(profileService.info());
    }

    /**
     * 修改
     */
    @PostMapping(value = {"/edit"})
    public Response<Void> edit(@Validated @RequestBody UserProfile userProfile) {
        profileService.edit(userProfile);
        return Response.success();
    }

    /**
     * 重置密码
     */
    @PostMapping(value = {"/passwd/reset"})
    public Response<Void> resetPasswd(@RequestBody UserProfile userProfile) {
        profileService.resetPasswd(userProfile);
        return Response.success();
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public Response<String> avatar(@RequestParam("file") MultipartFile file, @Validated SysAttach sysAttach) throws Exception {
        SysAttach avatar = sysAttachService.upload(file, sysAttach, true);
        sysAttachService.masterReserve(avatar.getMasterId(), avatar.getAttachGroup(), avatar.getAttachType(), 3);
        String url = sysAttachService.preview(avatar);
        return Response.success(url);
    }
}
