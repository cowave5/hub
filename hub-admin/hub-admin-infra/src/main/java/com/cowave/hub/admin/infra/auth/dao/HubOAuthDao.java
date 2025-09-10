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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.auth.HubOAuth;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthMapper;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class HubOAuthDao extends ServiceImpl<HubOAuthMapper, HubOAuth> {

    /**
     * 获取OAuth授权服务配置
     */
    public HubOAuth getByServerType(String tenantId, String serverType){
        return lambdaQuery().eq(HubOAuth::getTenantId, tenantId).eq(HubOAuth::getServerType, serverType).one();
    }

    /**
     * 删除OAuth授权服务配置
     */
    public void removeByServerType(String tenantId, String serverType){
        lambdaUpdate().eq(HubOAuth::getTenantId, tenantId).eq(HubOAuth::getServerType, serverType).remove();
    }
}
