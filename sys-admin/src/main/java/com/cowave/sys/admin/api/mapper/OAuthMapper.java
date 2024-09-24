/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import com.cowave.sys.admin.api.entity.oauth.OAuthConfig;
import com.cowave.sys.admin.api.entity.oauth.OAuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface OAuthMapper {

    /**
     * 保存授权配置
     */
    void saveConfig(OAuthConfig oAuthConfig);

    /**
     * 删除授权配置
     */
    void deleteConfig(String appType);

    /**
     * 获取授权配置
     */
    OAuthConfig getConfig(String appType);

    /**
     * 获取授权配置
     */
    List<OAuthConfig> listConfig();

    /**
     * 保存授权用户
     */
    void saveUser(OAuthUser oauthUser);

    /**
     * 获取授权用户
     */
    List<OAuthUser> listUser(OAuthUser oAuthUser);

    /**
     * 用户信息
     */
    OAuthUser infoUser(Long id);

    /**
     * 获取授权角色
     */
    String roleCode(Long userId);

    /**
     * 获取授权操作
     */
    List<String> permits(Long userId);

    /**
     * 修改用户角色
     */
    void changeUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId, @Param("updateTime") Date updateTime);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);
}
