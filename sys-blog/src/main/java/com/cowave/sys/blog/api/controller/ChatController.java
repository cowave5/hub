/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.sys.blog.api.entity.VerifyCode;
import com.cowave.sys.blog.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天室
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     * 聊天室验证码
     */
    @PostMapping("/verifyCode")
    public Response<Void> verifyCode(@RequestParam(value = "mailBox") String mailBox, @RequestParam(value = "uuid") String uuid) {
        chatService.verifyCode(mailBox, uuid);
        return Response.success();
    }

    /**
     * 登录聊天界面
     */
    @PostMapping("/loginChatRoom")
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> loginChatRoom(VerifyCode verifyCode) {
        chatService.loginChatRoom(verifyCode);
        return Response.success();
    }
}
