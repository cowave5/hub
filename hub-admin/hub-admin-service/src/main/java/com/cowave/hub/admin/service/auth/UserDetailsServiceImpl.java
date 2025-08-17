/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth;

import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.TenantUserDetailsService;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.infra.auth.MfaConfiguration;
import com.cowave.hub.admin.infra.auth.dao.UserDetailsDao;
import com.cowave.hub.admin.infra.rabc.dao.HubTenantDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.domain.rabc.HubUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;
import static com.cowave.hub.admin.domain.rabc.enums.EnableStatus.ENABLE;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements TenantUserDetailsService {
    private final HubUserDao hubUserDao;
    private final HubTenantDao hubTenantDao;
    private final UserDetailsDao userDetailsDao;
    private final MfaConfiguration mfaConfiguration;

    @Override
	public UserDetails loadTenantUserByUsername(String tenantId, String userAccount) {
        HubUser hubUser = hubUserDao.getByAccount(tenantId, UserType.SYS, userAccount);
        if(hubUser == null){
            return null;
        }
        HttpAsserts.equals(ENABLE, hubUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);

        // 租户
        HubTenant hubTenant = hubTenantDao.getById(hubUser.getTenantId());
        HttpAsserts.equals(ENABLE, hubTenant.getStatus(),
                FORBIDDEN, "{admin.user.tenant.disable}", hubTenant.getTenantName());
        if(hubTenant.getExpireTime() != null){
            HttpAsserts.isTrue(hubTenant.getExpireTime().after(new Date()),
                    FORBIDDEN, "{admin.user.tenant.expired}", hubTenant.getTenantName());
        }

        String mfaKey = hubUser.getMfa();
        if(StringUtils.isBlank(mfaKey)){
            return userDetailsDao.newUserDetails(UserType.SYS.getVal(), hubTenant, hubUser);
        }else{
            String mfaToken = mfaConfiguration.buildMfaToken(tenantId, userAccount);
            AccessUserDetails userDetails = new AccessUserDetails();
            userDetails.setUsername(userAccount);
            userDetails.setUserPasswd(hubUser.getUserPasswd());
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaToken);
            return userDetails;
        }
	}
}
