/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth.bo;

import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.cowave.sys.admin.domain.auth.AccessType.GITLAB;

/**
 * @author shanhuiming
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
        oauthUser.setServerType(GITLAB.val());
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
