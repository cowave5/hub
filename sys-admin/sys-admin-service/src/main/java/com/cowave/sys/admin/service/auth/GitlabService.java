package com.cowave.sys.admin.service.auth;

import com.cowave.commons.client.http.annotation.*;
import com.cowave.commons.client.http.response.HttpResponse;
import com.cowave.sys.admin.domain.auth.bo.GitlabToken;
import com.cowave.sys.admin.domain.auth.bo.GitlabUser;

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
