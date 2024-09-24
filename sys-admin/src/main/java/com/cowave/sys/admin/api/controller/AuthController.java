/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.helper.operation.Operation;
import com.cowave.commons.framework.helper.operation.OperationAccepter;
import com.cowave.sys.admin.api.entity.UserProfile;
import com.cowave.sys.admin.api.entity.UserRegister;
import com.cowave.sys.admin.api.entity.oauth.OAuthUser;
import com.cowave.sys.admin.api.service.OAuthService;
import com.cowave.sys.admin.api.service.SysAttachService;
import com.cowave.sys.admin.api.service.auth.captcha.CaptchaInfo;
import com.cowave.sys.admin.api.entity.Route;
import com.cowave.sys.admin.api.service.auth.AuthService;
import com.cowave.sys.admin.api.service.auth.captcha.CaptchaService;
import com.cowave.sys.model.admin.Login;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysLog;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cowave.sys.admin.api.entity.OnlineUser;
import com.cowave.commons.framework.support.redis.RedisHelper;

import lombok.RequiredArgsConstructor;

/**
 * 鉴权
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final RedisHelper redisHelper;

    private final SysAttachService attachService;

    private final CaptchaService captchaService;

    private final OAuthService oAuthService;

    private final OperationAccepter<SysLog> operationAccepter;

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Response<CaptchaInfo> captcha() throws IOException {
        return Response.success(captchaService.captcha());
    }

    /**
     * 获取邮箱验证码
     */
    @GetMapping("/captcha/email")
    public Response<Void> captchaEmail(String email) {
        captchaService.captchaEmail(email);
        return Response.success();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Response<String> register(@Validated @RequestBody UserRegister userRegister) {
        captchaService.validEmail(userRegister);
        return Response.success(authService.register(userRegister));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<AccessToken> login(@Validated @RequestBody Login login) {
        captchaService.valid(login);
        return Response.success(authService.login(login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 登录
     */
    @PostMapping("/logon")
    public Response<AccessToken> logon(@Validated @RequestBody Login login) {
        return Response.success(authService.login(login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 退出
     */
    @Operation(type = "admin_login", action = "logout", desc = "退出登录")
    @RequestMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) throws IOException {
    	authService.logout(response);
        return Response.success();
    }

    /**
     * 令牌刷新
     */
    @RequestMapping("/refresh")
    public void refresh(HttpServletResponse response, String refreshToken) throws Exception {
        authService.refresh(response, refreshToken);
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<UserProfile> info() throws Exception {
        AccessToken accessToken = Access.accessToken();
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(accessToken.getUserId());
        userProfile.setUserName(accessToken.getUserNick());
        userProfile.setRoles(accessToken.getRoles());
        userProfile.setPermissions(accessToken.getPermissions());
        if(AccessToken.TYPE_OAUTH.equals(accessToken.getType())){
            OAuthUser oAuthUser = oAuthService.infoUser(accessToken.getUserId());
            userProfile.setUserEmail(oAuthUser.getUserEmail());
            userProfile.setAvatar(oAuthUser.getUserAvatar());
        }else{
            SysAttach avatar = attachService.latestOfMaster(accessToken.getUserId(), "sys-user");
            if(avatar != null){
                userProfile.setAvatar(avatar.getViewUrl());
            }
        }
        return Response.success(userProfile);
    }

    /**
     * 路由信息
     */
    @GetMapping("/routes")
    public Response<List<Route>> routes() {
        return Response.success(authService.routes());
    }

    /**
     * 在线用户
     */
    @PostMapping("/online")
    public Response<Response.Page<OnlineUser>> onlineList(@RequestBody OnlineUser login) {
        List<OnlineUser> list = new ArrayList<>();
        for (String key : redisHelper.keys("Token:*")) {
            AccessToken accessToken = redisHelper.getValue(key);
            if(accessToken == null
                    || (login.getUserAccount() != null && !accessToken.getUsername().contains(login.getUserAccount()))
                    || (login.getBeginTime() != null && login.getBeginTime().after(accessToken.getAccessTime()))
                    || (login.getEndTime() != null && login.getEndTime().before(accessToken.getAccessTime()))){
                continue;
            }

            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setTokenId(accessToken.getId());
            onlineUser.setTokenType(accessToken.getType());
            onlineUser.setAccessIp(accessToken.getAccessIp());
            onlineUser.setAccessTime(accessToken.getAccessTime());
            onlineUser.setUserAccount(accessToken.getUsername());
            onlineUser.setUserName(accessToken.getUserNick());
            onlineUser.setLoginTime(accessToken.getLoginTime());
            onlineUser.setAccessCluster(accessToken.getClusterName());
            list.add(onlineUser);
        }
        return Response.page(list);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@permit.hasPermit('monitor:online:force')")
    @GetMapping("/outline/{tokenType}/{userAccount}")
    public Response<String> outline(@PathVariable String tokenType, @PathVariable String userAccount) {
        String tokenKey = AccessToken.KEY + tokenType + ":" + userAccount;
    	AccessToken accessToken = redisHelper.getValue(tokenKey);
    	if(accessToken == null){
    		return Response.success();
        }
    	redisHelper.delete(tokenKey);

        SysLog sysLog = new SysLog();
        sysLog.initialize();
        sysLog.recordOperation("admin_login", "logout_force", "强退用户：[" + accessToken.getUserNick() + "]");
        operationAccepter.accept(sysLog);
        return Response.success();
    }
}
