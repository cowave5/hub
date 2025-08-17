/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.auth.HubOAuthUser;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class HubOAuthUserDao extends ServiceImpl<HubOAuthUserMapper, HubOAuthUser> {

    public Page<HubOAuthUser> getUserPage(String tenantId, String serverType, String userAccount) {
        return lambdaQuery()
                .eq(HubOAuthUser::getTenantId, tenantId)
                .eq(HubOAuthUser::getServerType, serverType)
                .like(StringUtils.isNotBlank(userAccount), HubOAuthUser::getUserAccount, userAccount)
                .page(Access.page());
    }

    /**
     * 查询用户（账号）
     */
    public HubOAuthUser getByAccount(String tenantId, String serverType, String userAccount){
        return lambdaQuery()
                .eq(HubOAuthUser::getTenantId, tenantId)
                .eq(HubOAuthUser::getServerType, serverType)
                .eq(HubOAuthUser::getUserAccount, userAccount)
                .one();
    }
}
