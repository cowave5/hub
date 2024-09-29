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
import com.cowave.commons.framework.access.security.AccessTokenInfo;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.sys.admin.domain.auth.request.LdapRequest;
import com.cowave.sys.admin.domain.auth.vo.AuthInfo;
import com.cowave.sys.admin.domain.auth.vo.CaptchaInfo;
import com.cowave.sys.admin.domain.auth.request.LoginRequest;
import com.cowave.sys.admin.domain.auth.request.RegisterRequest;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.service.auth.*;
import com.cowave.sys.admin.domain.base.request.OnlineQuery;
import com.cowave.sys.admin.domain.rabc.vo.Route;
import com.cowave.sys.admin.service.base.SysAttachService;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.cowave.sys.admin.domain.rabc.enums.AccessType.GITLAB;
import static com.cowave.sys.admin.domain.rabc.enums.AccessType.SYS;

/**
 * 鉴权
 * @order 8
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final SysAttachService attachService;
    private final CaptchaService captchaService;
    private final AuthService authService;
    private final LdapService ldapService;
    private final OAuthService oauthService;
    private final BearerTokenService bearerTokenService;

    /**
     * 验证码
     */
    @GetMapping("/public/captcha")
    public Response<CaptchaInfo> captcha() throws IOException {
        return Response.success(captchaService.captcha());
    }

    /**
     * 邮箱验证码
     */
    @GetMapping("/public/captcha/email")
    public Response<Void> captchaEmail(@NotNull(message = "{admin.user.email.notnull}") String email) throws Exception {
        return Response.success(() -> captchaService.captchaEmail(email));
    }

    /**
     * 注册
     */
    @PostMapping("/public/register")
    public Response<String> register(@Validated @RequestBody RegisterRequest request) {
        captchaService.validEmail(request);
        return Response.success(authService.register(request));
    }

    /**
     * 登录
     */
    @PostMapping("/public/logon")
    public Response<AccessUserDetails> logon(@Validated @RequestBody LoginRequest request) {
        return Response.success(authService.login(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * 登录（验证码）
     */
    @PostMapping("/public/login")
    public Response<AccessUserDetails> login(@Validated @RequestBody LoginRequest request) {
        captchaService.validCaptcha(request);
        return Response.success(authService.login(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * Ldap认证
     */
    @PostMapping("/public/ldap")
    public Response<AccessUserDetails> ldap(@Validated @RequestBody LdapRequest request) {
        return Response.success(ldapService.authenticate(request.getUserAccount(), request.getPassWord()));
    }

    /**
     * Gitlab回调认证
     */
    @GetMapping("/public/gitlab")
    public Response<AccessUserDetails> gitlabCallback(@RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(code));
    }

    /**
     * 退出
     */
    @GetMapping("/logout")
    public Response<Void> logout() throws Exception {
        return Response.success(authService::logout);
    }

    /**
     * 令牌刷新
     */
    @GetMapping("/public/refresh")
    public Response<AccessUserDetails> refresh(@NotNull(message = "{admin.refreshToken.notnull}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthInfo> info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Integer userId = userDetails.getUserId();

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);
        authInfo.setUserName(userDetails.getUserNick());
        authInfo.setRoles(userDetails.getRoles());
        authInfo.setPermissions(userDetails.getPermissions());
        if(GITLAB.isEqual(userDetails.getType())){
            OAuthUser oAuthUser = oauthService.infoUser(userId);
            authInfo.setUserEmail(oAuthUser.getUserEmail());
            authInfo.setAvatar(oAuthUser.getUserAvatar());
        }else if(SYS.isEqual(userDetails.getType())){
            SysAttach avatar = attachService.latestOfMaster(Long.valueOf(userId), "sys-user");
            if(avatar != null){
                authInfo.setAvatar(avatar.getViewUrl());
            }
        }
        return Response.success(authInfo);
    }

    /**
     * 在线用户
     */
    @PostMapping("/online")
    public Response<Response.Page<AccessTokenInfo>> onlineList(@RequestBody OnlineQuery login) {
        List<AccessTokenInfo> list = bearerTokenService.listAccessToken(
                login.getUserAccount(), login.getBeginTime(), login.getEndTime());
        list.sort(Comparator.comparing(AccessTokenInfo::getAccessTime).reversed());
        return Response.page(list);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@permit.hasPermit('monitor:online:force')")
    @GetMapping("/outline/{accessId}")
    public Response<Void> forceLogout(@PathVariable String accessId) throws Exception {
        return Response.success(() -> authService.forceLogout(accessId));
    }

    /**
     * 路由权限
     */
    @GetMapping("/routes")
    public Response<List<Route>> routes() {
        return Response.success(authService.routes());
    }
}
