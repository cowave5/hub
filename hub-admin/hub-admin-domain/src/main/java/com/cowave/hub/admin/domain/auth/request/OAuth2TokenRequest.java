/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Data
public class OAuth2TokenRequest {

    /**
     * 客户端id
     */
    @NotBlank(message = "client_id can't be null")
    @JsonProperty("client_id")
    private String clientId;

    /**
     * 客户端密钥（如果密钥无法保证安全需要额外添加PKCE机制）
     */
    @NotBlank(message = "client_secret can't be null")
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * 回调地址，需要一致（防止重定向攻击）
     */
    @NotBlank(message = "redirect_uri can't be null")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    /**
     * 授权码
     */
    @NotBlank(message = "code can't be null")
    private String code;

    /**
     * PKCE
     */
    @JsonProperty("code_verifier")
    private String codeVerifier;

    /**
     * 授权范围
     */
    private String scope;
}
