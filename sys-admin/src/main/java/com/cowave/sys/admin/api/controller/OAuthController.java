/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.sys.admin.api.entity.oauth.OAuthConfig;
import com.cowave.sys.admin.api.entity.oauth.OAuthUser;
import com.cowave.sys.admin.api.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * OAuth授权
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 保存授权配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:edit')")
    @PostMapping("/config/save")
    public Response<Void> saveConfig(@RequestBody OAuthConfig oAuthConfig) {
        oauthService.saveConfig(oAuthConfig);
        return Response.success();
    }

    /**
     * 获取授权配置
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/get/{appType}")
    public Response<OAuthConfig> getConfig(@PathVariable String appType) {
        return Response.success(oauthService.getConfig(appType));
    }

    /**
     * 获取授权用户
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:query')")
    @PostMapping("/user/list")
    public Response<Response.Page<OAuthUser>> userList(@RequestBody OAuthUser oAuthUser) {
        return Response.page(oauthService.listUser(oAuthUser));
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:edit')")
    @GetMapping("/user/role/change")
    public Response<Void> changeUserRole(
            @NotNull(message = "{user.notnull.id}") Long userId, @NotNull(message = "{role.notnull.id}") Long roleId) {
        oauthService.changeUserRole(userId, roleId);
        return Response.success();
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('oauth:gitlab:user:delete')")
    @GetMapping("/user/delete")
    public Response<Void> deleteUser(@NotNull(message = "{user.notnull.id}") Long userId) {
        oauthService.deleteUser(userId);
        return Response.success();
    }

    /**
     * gitlab回调
     */
    @GetMapping("/callback/gitlab")
    public Response<AccessToken> gitlabCallback(@RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(code));
    }
}
