/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Data
public class LoginRequest {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 用户名
     */
	@NotBlank(message = "{admin.user.account.notnull}")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "{admin.user.passwd.notnull}")
    private String passWord;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码Id
     */
    private String captchaId;
}
