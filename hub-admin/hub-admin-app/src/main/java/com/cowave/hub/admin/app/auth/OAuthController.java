/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.domain.auth.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.HubOAuth;
import com.cowave.hub.admin.domain.auth.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.hub.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.hub.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.vo.OAuth2CodeVo;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * OAuth授权
 * @order 11
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 获取授权服务配置
     *
     * @param serverType 服务类型
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/{serverType}")
    public Response<HubOAuth> getOauth(@PathVariable String serverType) {
        return Response.success(oauthService.getOauth(Access.tenantId(), serverType));
    }

    /**
     * 修改授权服务配置
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:edit')")
    @PatchMapping("/config")
    public Response<Void> editOauth(@RequestBody HubOAuth oauth) throws Exception {
        return Response.success(() -> oauthService.editOauth(Access.tenantId(), oauth));
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:user:query')")
    @GetMapping("/user")
    public Response<Response.Page<HubOAuthUser>> listUser(OAuthUserQuery query) {
        return Response.page(oauthService.listUser(Access.tenantId(), query));
    }

    /**
     * 授权客户端列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:client:query')")
    @GetMapping("/client")
    public Response<Response.Page<HubOAuthApp>> listClient(String clientName) {
        return Response.page(oauthService.listClient(Access.tenantId(), clientName));
    }

    /**
     * 新增授权客户端
     */
    @PreAuthorize("@permits.hasPermit('oauth:client:create')")
    @PostMapping("/client")
    public Response<HubOAuthApp> createClient(@RequestBody HubOAuthApp hubOAuthApp) {
        return Response.success(oauthService.createClient(Access.tenantId(), hubOAuthApp));
    }

    /**
     * 删除授权客户端
     */
    @PreAuthorize("@permits.hasPermit('oauth:client:delete')")
    @DeleteMapping("/client/{ids}")
    public Response<Void> deleteClient(@PathVariable List<Integer> ids) throws Exception {
        return Response.success(() -> oauthService.deleteClient(Access.tenantId(), ids));
    }

    /**
     * 客户端获取授权码
     */
	@PostMapping("/client/authorize/code")
	public Response<OAuth2CodeVo> getClientCode(@Validated @RequestBody OAuth2CodeRequest request){
		return Response.success(oauthService.getClientCode(request));
	}

    /**
     * 客户端地址回调
     */
	@GetMapping("/client/redirect/{code}")
	public void clientRedirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        oauthService.clientRedirect(code, response);
	}

    /**
     * 客户端获取令牌
     */
	@PostMapping("/client/authorize/token")
	public Response<AccessUserDetails> getClientToken(@Validated @RequestBody OAuth2TokenRequest request){
		return Response.success(oauthService.getClientToken(request));
	}
}
