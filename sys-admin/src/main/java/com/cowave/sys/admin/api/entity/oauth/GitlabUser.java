/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class GitlabUser {

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户头像
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * Ldap信息
     */
    private List<LdapInfo> identities;

    @Data
    public static class LdapInfo {

        private String provider;

        @JsonProperty("extern_uid")
        private String externUid;
    }

    public static OAuthUser oAuthUser(GitlabUser gitlabUser){
        OAuthUser oauthUser = new OAuthUser();
        oauthUser.setAppType(OAuthConfig.APP_GITLAB);
        oauthUser.setUserName(gitlabUser.name);
        oauthUser.setUserAccount(gitlabUser.username);
        oauthUser.setUserEmail(gitlabUser.email);
        oauthUser.setUserAvatar(gitlabUser.avatarUrl);
        if(CollectionUtils.isNotEmpty(gitlabUser.identities)){
            LdapInfo ldap = gitlabUser.identities.get(0);
            oauthUser.setUserDept(ldap.externUid);
        }
        return oauthUser;
    }
}
