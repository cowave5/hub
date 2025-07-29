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
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.request.LdapLogin;
import com.cowave.sys.admin.domain.auth.request.MfaLogin;
import com.cowave.sys.admin.domain.auth.vo.AuthInfo;
import com.cowave.sys.admin.domain.auth.vo.CaptchaInfo;
import com.cowave.sys.admin.domain.auth.request.Login;
import com.cowave.sys.admin.domain.auth.request.RegisterRequest;
import com.cowave.sys.admin.domain.auth.vo.OnlineInfo;
import com.cowave.sys.admin.service.auth.*;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 鉴权
 *
 * @author shanhuiming
 * @order 9
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CaptchaService captchaService;
    private final AuthService authService;
    private final LdapService ldapService;
    private final OAuthService oauthService;

    /**
     * 验证码
     */
    @GetMapping("/public/captcha")
    public Response<CaptchaInfo> captcha(@NotNull(message = "{admin.tenant.id.null}") String tenantId) throws IOException {
        return Response.success(captchaService.captcha(tenantId));
    }

    /**
     * 邮箱验证码
     */
    @GetMapping("/public/captcha/email")
    public Response<Void> captchaEmail(@NotNull(message = "{admin.user.email.null}") String email) throws Exception {
        return Response.success(() -> captchaService.captchaEmail(email));
    }

    /**
     * 注册
     */
    @PostMapping("/public/register")
    public Response<String> register(@Validated @RequestBody RegisterRequest request) {
        captchaService.validEmail(request.getUserEmail(), request.getCaptcha());
        return Response.success(authService.register(request));
    }

    /**
     * 登录
     */
    @PostMapping("/public/logon")
    public Response<AccessUserDetails> logon(@Validated @RequestBody Login login) {
        return Response.success(authService.login(login.getTenantId(), login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 登录（验证码）
     */
    @PostMapping("/public/login")
    public Response<AccessUserDetails> login(@Validated @RequestBody Login login) {
        captchaService.validCaptcha(login.getTenantId(), login.getCaptchaId(), login.getCaptcha());
        return Response.success(authService.login(login.getTenantId(), login.getUserAccount(), login.getPassWord()));
    }

    /**
     * MFA认证
     */
    @PostMapping("/public/mfa")
    public Response<AccessUserDetails> mfa(@Validated @RequestBody MfaLogin mfaLogin) {
        return Response.success(authService.mfa(mfaLogin.getMfaToken(), mfaLogin.getMfaCode()));
    }

    /**
     * Ldap认证
     */
    @PostMapping("/public/ldap")
    public Response<AccessUserDetails> ldap(@Validated @RequestBody LdapLogin login) {
        return Response.success(ldapService.authenticate(login.getTenantId(), login.getUserAccount(), login.getPassWord()));
    }

    /**
     * Gitlab回调认证
     */
    @GetMapping("/public/gitlab")
    public Response<AccessUserDetails> gitlabCallback(
            @RequestParam("tenantId") String tenantId, @RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(tenantId, code));
    }

    /**
     * 令牌刷新
     */
    @GetMapping("/public/refresh")
    public Response<AccessUserDetails> refresh(
            @NotNull(message = "{admin.refreshToken.null}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 退出
     */
    @GetMapping("/logout")
    public Response<Void> logout() throws Exception {
        return Response.success(authService::logout);
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthInfo> getAuthInfo() throws Exception {
        return Response.success(authService.getAuthInfo());
    }

    /**
     * 菜单权限
     */
    @GetMapping("/menus")
    public Response<List<Route>> routes() {
        return Response.success(authService.menus());
    }

    /**
     * 在线用户
     */
    @PostMapping("/online")
    public Response<Response.Page<OnlineInfo>> onlineList() {
        return Response.page(authService.onlineList());
    }

    /**
     * 撤销Access令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/access")
    public Response<Void> revokeAccess(String type, String account, String id) throws Exception {
        return Response.success(() -> authService.revokeAccess(Access.tenantId(), type, account, id));
    }

    /**
     * 撤销Refresh令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/refresh")
    public Response<Void> revokeRefresh(String type, String account) throws Exception {
        return Response.success(() -> authService.revokeRefresh(Access.tenantId(), type, account));
    }
}
