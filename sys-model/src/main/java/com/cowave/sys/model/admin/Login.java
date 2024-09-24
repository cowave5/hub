/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author shanhuiming
 *
 */
@NoArgsConstructor
@Data
public class Login {

    /**
     * 用户名
     */
	@NotBlank(message = "user.notnull.account")
    private String userAccount;

    /**
     * 用户密码
     */
    @JSONField(serialize = false)
    @NotBlank(message = "user.notnull.passwd")
    private String passWord;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码Id
     */
    private String captchaId;

    public Login(String userAccount, String passWord){
        this.userAccount = userAccount;
        this.passWord = passWord;
    }
}
