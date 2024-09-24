/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class UserRegister {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "user.notnull.account")
    private String userAccount;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "user.notnull.email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "user.invalid.email")
    private String userEmail;

    /**
     * 用户密码
     */
    private String userPasswd;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 验证码
     */
    private String captcha;
}
