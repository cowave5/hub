/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.sys.blog.api.entity.VerifyCode;

/**
 *
 * @author shanhuiming
 *
 */
public interface ChatService {

    /**
     * 验证码
     */
    void verifyCode(String mailBox, String uuid);

    /**
     * 登录
     */
    void loginChatRoom(VerifyCode verifyCode);
}
