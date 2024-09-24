/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.sys.admin.api.entity.oauth.OAuthConfig;
import com.cowave.sys.admin.api.entity.oauth.OAuthUser;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface OAuthService {

    /**
     * 保存授权配置
     */
    void saveConfig(OAuthConfig oAuthConfig);

    /**
     * OAuth配置获取
     */
    OAuthConfig getConfig(String appType);

    /**
     * 获取授权配置
     */
    List<OAuthConfig> listConfig();

    /**
     * 获取授权用户
     */
    List<OAuthUser> listUser(OAuthUser oAuthUser);

    /**
     * 用户信息
     */
    OAuthUser infoUser(Long id);

    /**
     * 修改用户角色
     */
    void changeUserRole(Long userId, Long roleId);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * gitlab回调
     */
    AccessToken gitlabCallback(String code);
}
