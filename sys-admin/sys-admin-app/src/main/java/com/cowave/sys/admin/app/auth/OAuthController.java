/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.OAuthClient;
import com.cowave.sys.admin.domain.auth.OAuthServer;
import com.cowave.sys.admin.domain.auth.dto.OAuthUserDto;
import com.cowave.sys.admin.domain.auth.request.OAuth2CodeRequest;
import com.cowave.sys.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.sys.admin.domain.auth.request.OAuthUserQuery;
import com.cowave.sys.admin.domain.auth.vo.OAuth2CodeVo;
import com.cowave.sys.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * OAuth配置
 * @order 25
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
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/{serverType}")
    public Response<OAuthServer> getServerConfig(@PathVariable String serverType) {
        return Response.success(oauthService.getServerConfig(serverType));
    }

    /**
     * 修改授权服务配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:edit')")
    @PatchMapping("/config")
    public Response<Void> updateServerConfig(@RequestBody OAuthServer oAuthServer) throws Exception {
        return Response.success(() -> oauthService.updateServerConfig(oAuthServer));
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:query')")
    @GetMapping("/user")
    public Response<Response.Page<OAuthUserDto>> listUser(OAuthUserQuery query) {
        return Response.page(oauthService.listUser(query));
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:delete')")
    @DeleteMapping("/user/{userId}")
    public Response<Void> deleteUser(@PathVariable Integer userId) throws Exception {
        return Response.success(() -> oauthService.deleteUser(userId));
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户id
     * @param roleCode 角色编码
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:edit')")
    @PatchMapping("/user/role/{userId}/{roleCode}")
    public Response<Void> updateUserRole(@PathVariable Integer userId, @PathVariable String roleCode) throws Exception {
        return Response.success(() -> oauthService.updateUserRole(userId, roleCode));
    }

    /**
     * 授权客户端列表
     */
    @PreAuthorize("@permit.hasPermit('oauth:client:query')")
    @GetMapping("/client")
    public Response<Response.Page<OAuthClient>> listClient(String clientName) {
        return Response.page(oauthService.listClient(clientName));
    }

    /**
     * 新增授权客户端
     */
    @PreAuthorize("@permit.hasPermit('oauth:client:create')")
    @PostMapping("/client")
    public Response<OAuthClient> createClient(@RequestBody OAuthClient oAuthClient) {
        return Response.success(oauthService.createClient(oAuthClient));
    }

    /**
     * 删除授权客户端
     */
    @PreAuthorize("@permit.hasPermit('oauth:client:delete')")
    @DeleteMapping("/client/{ids}")
    public Response<Void> deleteClient(@PathVariable List<Integer> ids) throws Exception {
        return Response.success(() -> oauthService.deleteClient(ids));
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
