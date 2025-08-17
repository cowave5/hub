/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth;

import com.cowave.commons.client.http.annotation.*;
import com.cowave.commons.client.http.response.HttpResponse;
import com.cowave.hub.admin.domain.auth.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.bo.GitlabUser;

import static com.cowave.commons.client.http.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient
public interface GitlabService {

    /**
     * 获取Gitlab令牌
     */
    @HttpLine("POST /oauth/token?client_id={clientId}&client_secret={clientSecret}&redirect_uri={redirectUri}&grant_type={grantType}&scope={scope}&code={code}")
    HttpResponse<GitlabToken> getGitlabToken(@HttpHost String gitlabUrl,
                                             @HttpParam("clientId") String clientId, @HttpParam("clientSecret") String clientSecret,
                                             @HttpParam("redirectUri") String redirectUri, @HttpParam("grantType") String grantType,
                                             @HttpParam("scope") String scope, @HttpParam("code") String code);

    /**
     * 获取Gitlab用户
     */
    @HttpHeaders({Authorization + ": Bearer {accessToken}"})
    @HttpLine("GET /api/v4/user")
    HttpResponse<GitlabUser> getGitlabUser(@HttpHost String gitlabUrl, @HttpParam("accessToken") String accessToken);
}
