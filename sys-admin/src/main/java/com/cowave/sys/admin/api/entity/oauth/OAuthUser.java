/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.oauth;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 授权用户
 */
@Data
public class OAuthUser {

    /**
     * id
     */
    private Long id;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 用户角色名称
     */
    private String roleName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime = new Date();

    public static AccessToken accessToken(OAuthUser oauthUser){
        AccessToken accessToken = AccessToken.newToken();
        accessToken.setType(AccessToken.TYPE_OAUTH);
        accessToken.setUserId(oauthUser.id);
        accessToken.setUsername(oauthUser.userAccount);
        accessToken.setUserNick(oauthUser.userName);
        accessToken.setDeptName(oauthUser.userDept);
        return accessToken;
    }
}
