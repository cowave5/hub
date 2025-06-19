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
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import com.cowave.sys.admin.domain.rabc.SysUserAdmin;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.OAuthUserDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysUserAdminDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.service.auth.ProfileService;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.LdapUserDtoMapper;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.sys.admin.domain.auth.AccessType.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final SysAttachService attachService;
    private final SysUserDao sysUserDao;
    private final SysUserAdminDao sysUserAdminDao;
    private final SysUserDtoMapper sysUserDtoMapper;
    private final LdapUserDtoMapper ldapUserDtoMapper;
    private final OAuthUserDtoMapper oauthUserDtoMapper;

    @Override
    public UserProfile info() throws Exception {
        Integer userId = Access.userId();
        String userCode = Access.userCode();
        if (GITLAB.equalsType(userCode)) {
            return oauthUserDtoMapper.getOauthProfile(userId);
        } else if (LDAP.equalsType(userCode)) {
            return ldapUserDtoMapper.getLdapUserProfile(userId);
        } else if (ADMIN.equalsType(userCode)) {
            SysUserAdmin userAdmin = sysUserAdminDao.getById(userId);
            UserProfile userProfile = userAdmin.newUserProfile();
            SysAttach avatar = attachService.latestOfMaster(Long.valueOf(userId), "admin-user");
            if (avatar != null) {
                userProfile.setAvatar(avatar.getViewUrl());
            }
            return userProfile;
        } else {
            UserProfile userProfile = sysUserDtoMapper.getUserProfile(userId);
            SysAttach avatar = attachService.latestOfMaster(Long.valueOf(userId), "sys-user");
            if (avatar != null) {
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
        String passwd;
        String userCode = Access.userCode();
        if (ADMIN.equalsType(userCode)) {
            passwd = sysUserAdminDao.getByUserCode(userCode).getUserPasswd();
        } else {
            passwd = sysUserDao.getByUserCode(userCode).getUserPasswd();
        }

        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        if (ADMIN.equalsType(userCode)) {
            sysUserAdminDao.updatePasswdByUserCode(userCode, passwordEncoder.encode(passwdReset.getNewPasswd()));
        } else {
            sysUserDao.updatePasswdById(Access.userId(), passwordEncoder.encode(passwdReset.getNewPasswd()));
        }
    }

    @Override
    public String uploadAvatar(MultipartFile file, AttachUpload attachUpload) throws Exception {
        String userCode = Access.userCode();
        if (ADMIN.equalsType(userCode)) {
            attachUpload.setAttachGroup("admin-user");
        } else if (SYS.equalsType(userCode)) {
            attachUpload.setAttachGroup("sys-user");
        }
        SysAttach attach = attachService.upload(file, attachUpload);
        attachService.masterReserve(attach.getMasterId(), attach.getAttachGroup(), attach.getAttachType(), 3);
        return attachService.preview(attach);
    }
}
