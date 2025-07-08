/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.client;

import com.cowave.commons.client.http.annotation.*;
import com.cowave.commons.client.http.invoke.codec.decoder.ResponseDecoder;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import com.cowave.sys.admin.domain.auth.request.OAuth2TokenRequest;

import static com.cowave.commons.client.http.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient(decoder = ResponseDecoder.class)
public interface AdminOAuthClient {

    /**
     * 获取授权令牌
     */
    @HttpLine("POST /admin/api/v1/oauth/client/authorize/token")
    AccessUserDetails getAuthorizeToken(@HttpHost String httpUrl, OAuth2TokenRequest request);

    /**
     * 获取用户信息
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/profile")
    UserProfile getUserProfile(@HttpHost String httpUrl, @HttpParam("accessToken") String accessToken);
}
