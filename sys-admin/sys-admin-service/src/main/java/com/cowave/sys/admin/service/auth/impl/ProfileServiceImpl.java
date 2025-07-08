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
import com.cowave.sys.admin.domain.auth.request.MfaBind;
import com.cowave.sys.admin.domain.auth.request.PasswdReset;
import com.cowave.sys.admin.domain.auth.request.ProfileUpdate;
import com.cowave.sys.admin.domain.auth.vo.MfaVo;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.infra.auth.MfaAuthVerifier;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.OAuthUserDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.service.auth.ProfileService;
import com.cowave.sys.admin.infra.auth.dao.mapper.dto.LdapUserDtoMapper;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.sys.admin.domain.auth.AuthType.*;
import static com.cowave.sys.admin.domain.base.constants.AttachType.AVATAR;
import static com.cowave.sys.admin.domain.base.constants.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final SysAttachService attachService;
    private final SysTenantDao sysTenantDao;
    private final SysUserDao sysUserDao;
    private final SysUserDtoMapper sysUserDtoMapper;
    private final LdapUserDtoMapper ldapUserDtoMapper;
    private final OAuthUserDtoMapper oauthUserDtoMapper;

    @Override
    public UserProfile info() throws Exception {
        String tenantId = Access.tenantId();
        Integer userId = Access.userId();
        String userCode = Access.userCode();
        UserProfile userProfile;
        if (GITLAB.equalsType(userCode)) {
            userProfile = oauthUserDtoMapper.getOauthProfile(userId);
        } else if (LDAP.equalsType(userCode)) {
            userProfile = ldapUserDtoMapper.getLdapUserProfile(userId);
        } else {
            userProfile = sysUserDtoMapper.getUserProfile(userId);
            SysAttach avatar = attachService.latestOfOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                userProfile.setAvatar(avatar.getViewUrl());
            }
        }
        // 填充租户信息
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        userProfile.setTenantId(sysTenant.getTenantId());
        userProfile.setTenantName(sysTenant.getTenantName());
        return userProfile;
    }

    @Override
    public void edit(ProfileUpdate profile) throws Exception {
        sysUserDao.updateProfileById(Access.userId(), profile);
        attachService.reserveByOwner(Access.userId(), SYSTEM_USER, AVATAR, 3);
    }

    @Override
    public void resetPasswd(PasswdReset passwdReset) {
        String userCode = Access.userCode();
        String passwd = sysUserDao.getByCode(userCode).getUserPasswd();
        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        sysUserDao.updatePasswdById(Access.userId(), passwordEncoder.encode(passwdReset.getNewPasswd()));
    }

    @Override
    public MfaVo generateMfa() {
        MfaVo mfaVo = new MfaVo();
        SysUser sysUser = sysUserDao.getByCode(Access.userCode());
        if(sysUser != null){
            String mfaKey = sysUser.getMfa();
            if(StringUtils.isBlank(mfaKey)){
                mfaKey = MfaAuthVerifier.generateKey();
                String mfaUrl = MfaAuthVerifier.generateAuthUrl(Access.tenantId(), Access.userAccount(), mfaKey);
                mfaVo.setMfaUrl(mfaUrl);
            }
            mfaVo.setMfaKey(mfaKey);
        }
        return mfaVo;
    }

    @Override
    public void enableMfa(MfaBind mfaBind) {
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(
                mfaBind.getMfaKey(), mfaBind.getMfaCode()), BAD_REQUEST, "{admin.mfa.code.invalid}");
        sysUserDao.setMfa(Access.userId(), mfaBind.getMfaKey());
    }

    @Override
    public void disableMfa(MfaBind mfaBind) {
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(
                mfaBind.getMfaKey(), mfaBind.getMfaCode()), BAD_REQUEST, "{admin.mfa.code.invalid}");
        sysUserDao.deleteMfa(Access.userId());
    }
}
