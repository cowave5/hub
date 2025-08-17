/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth.impl;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.domain.auth.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.dto.UserProfile;
import com.cowave.hub.admin.domain.auth.request.MfaBind;
import com.cowave.hub.admin.domain.auth.request.PasswdReset;
import com.cowave.hub.admin.domain.auth.request.ProfileUpdate;
import com.cowave.hub.admin.domain.auth.vo.MfaVo;
import com.cowave.hub.admin.domain.sys.HubAttach;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.infra.auth.MfaAuthVerifier;
import com.cowave.hub.admin.infra.auth.dao.HubOAuthUserDao;
import com.cowave.hub.admin.infra.rabc.dao.HubTenantDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubUserDtoMapper;
import com.cowave.hub.admin.service.auth.ProfileService;
import com.cowave.hub.admin.service.sys.HubAttachService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final HubAttachService attachService;
    private final HubTenantDao hubTenantDao;
    private final HubUserDao hubUserDao;
    private final HubOAuthUserDao oauthUserDaoHub;
    private final HubUserDtoMapper hubUserDtoMapper;

    @Override
    public UserProfile info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        String tenantId = userDetails.getTenantId();
        Integer userId = userDetails.getUserId();
        String userCode = userDetails.getUserCode();
        UserProfile userProfile = hubUserDtoMapper.getUserProfile(userId);
        // Avatar
        if (UserType.GITLAB.equalsType(userCode)) {
            HubOAuthUser hubOAuthUser =
                    oauthUserDaoHub.getByAccount(tenantId, UserType.GITLAB.getVal(), userDetails.getUsername());
            userProfile.setAvatar(hubOAuthUser.getUserAvatar());
        } else if (UserType.SYS.equalsType(userCode)){
            HubAttach avatar = attachService.latestOfOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                userProfile.setAvatar(avatar.getViewUrl());
            }
        }
        // 租户信息
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        userProfile.setTenantId(hubTenant.getTenantId());
        userProfile.setTenantName(hubTenant.getTenantName());
        return userProfile;
    }

    @Override
    public void edit(ProfileUpdate profile) throws Exception {
        hubUserDao.updateProfileById(Access.userId(), profile);
        attachService.reserveByOwner(Access.userId(), SYSTEM_USER, AVATAR, 3);
    }

    @Override
    public void resetPasswd(PasswdReset passwdReset) {
        String userCode = Access.userCode();
        String passwd = hubUserDao.getByCode(userCode).getUserPasswd();
        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        hubUserDao.updatePasswdById(Access.userId(), passwordEncoder.encode(passwdReset.getNewPasswd()));
    }

    @Override
    public MfaVo generateMfa() {
        MfaVo mfaVo = new MfaVo();
        HubUser hubUser = hubUserDao.getByCode(Access.userCode());
        if(hubUser != null){
            String mfaKey = hubUser.getMfa();
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
        hubUserDao.setMfa(Access.userId(), mfaBind.getMfaKey());
    }

    @Override
    public void disableMfa(MfaBind mfaBind) {
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(
                mfaBind.getMfaKey(), mfaBind.getMfaCode()), BAD_REQUEST, "{admin.mfa.code.invalid}");
        hubUserDao.deleteMfa(Access.userId());
    }
}
