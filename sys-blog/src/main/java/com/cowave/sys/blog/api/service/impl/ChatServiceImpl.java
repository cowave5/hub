/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.support.redis.RedisHelper;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.blog.api.entity.VerifyCode;
import com.cowave.sys.blog.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private static final String CAPTCHA_KEY = "chat:";

    private final RedisHelper redisHelper;

    private final JavaMailSender mailSender;

    @Override
    public void verifyCode(String mailBox, String uuid) {
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("cowaveAdmin@163.com");
        mailMessage.setTo(mailBox);
        mailMessage.setSubject("聊天室验证码");
        mailMessage.setText("验证码:" + code + "，有效期为3分钟");
        mailSender.send(mailMessage);
        redisHelper.putExpireValue(CAPTCHA_KEY + uuid, code, 3, TimeUnit.MINUTES);
    }

    @Override
    public void loginChatRoom(VerifyCode verifyCode) {
        String code = redisHelper.getValue(CAPTCHA_KEY + verifyCode.getUuid());
        Asserts.notNull(code, "验证码已过期");
        Asserts.equals(code, verifyCode.getCode(), "验证码错误");
        Access.setCookie("chat_token", verifyCode.getNickName() + ":" + verifyCode.getUuid());
    }
}
