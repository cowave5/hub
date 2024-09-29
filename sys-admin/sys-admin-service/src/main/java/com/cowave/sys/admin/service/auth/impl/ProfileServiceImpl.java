/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth.impl;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import com.cowave.sys.admin.domain.auth.request.PasswdReset;
import com.cowave.sys.admin.domain.auth.request.ProfileUpdate;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.OAuthUserDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.service.auth.ProfileService;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.LdapUserDtoMapper;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.sys.admin.domain.rabc.enums.AccessType.LDAP;
import static com.cowave.sys.admin.domain.rabc.enums.AccessType.GITLAB;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final SysAttachService attachService;
    private final SysUserDao sysUserDao;
    private final SysUserDtoMapper sysUserDtoMapper;
    private final LdapUserDtoMapper ldapUserDtoMapper;
    private final OAuthUserDtoMapper oauthUserDtoMapper;

    @Override
    public UserProfile info() throws Exception {
        Integer userId = Access.userId();
        String userCode = Access.userCode();
        if(GITLAB.isTypeEquals(userCode)){
            return oauthUserDtoMapper.getOauthProfile(userId);
        }else if(LDAP.isTypeEquals(userCode)){
            return ldapUserDtoMapper.getLdapUserProfile(userId);
        }else{
            UserProfile userProfile = sysUserDtoMapper.getUserProfile(userId);
            SysAttach avatar = attachService.latestOfMaster(Long.valueOf(userId), "sys-user");
            if(avatar != null){
                userProfile.setAvatar(avatar.getViewUrl());
            }
            return userProfile;
        }
    }

    @Override
    public void edit(ProfileUpdate profile) {
        sysUserDao.updateProfileById(Access.userId(), profile);
    }

    @Override
    public void resetPasswd(PasswdReset passwdReset) {
        String passwd = sysUserDao.queryPasswdById(Access.userId());
        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        sysUserDao.updatePasswdById(Access.userId(), passwordEncoder.encode(passwdReset.getNewPasswd()));
    }
}
