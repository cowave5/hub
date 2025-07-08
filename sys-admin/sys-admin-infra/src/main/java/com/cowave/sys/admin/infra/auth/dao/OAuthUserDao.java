/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.infra.auth.dao.mapper.OAuthUserMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class OAuthUserDao extends ServiceImpl<OAuthUserMapper, OAuthUser> {

    /**
     * 查询用户（账号）
     */
    public OAuthUser getByAccount(String tenantId, String serverType, String userAccount){
        return lambdaQuery()
                .eq(OAuthUser::getTenantId, tenantId)
                .eq(OAuthUser::getServerType, serverType)
                .eq(OAuthUser::getUserAccount, userAccount)
                .one();
    }

    /**
     * 查询用户（编码）
     */
    public OAuthUser getByUserCode(String userCode){
        return lambdaQuery().eq(OAuthUser::getUserCode, userCode).one();
    }

    /**
     * 删除用户
     */
    public void removeById(String tenantId, Integer userId){
        lambdaUpdate().eq(OAuthUser::getTenantId, tenantId).eq(OAuthUser::getId, userId).remove();
    }

    /**
     * 更新用户角色
     */
    public void updateRoleCodeById(String tenantId, Integer userId, String roleCode){
        lambdaUpdate()
                .eq(OAuthUser::getTenantId, tenantId)
                .eq(OAuthUser::getId, userId)
                .set(OAuthUser::getRoleCode, roleCode)
                .set(OAuthUser::getUpdateTime, new Date())
                .update();
    }

    /**
     * 查询用户名称
     */
    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(OAuthUser::getUserCode, userCode)
                .select(OAuthUser::getUserName)
                .oneOpt().map(OAuthUser::getUserName).orElse(null);
    }

    /**
     * 查询用户名称
     */
    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<OAuthUser> list = lambdaQuery()
                .in(OAuthUser::getUserCode, userCodes)
                .select(OAuthUser::getUserCode, OAuthUser::getUserName)
                .list();
        return Collections.copyToMap(list, OAuthUser::getUserCode, OAuthUser::getUserName);
    }
}
