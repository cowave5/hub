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
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.infra.auth.dao.mapper.LdapUserMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class LdapUserDao extends ServiceImpl<LdapUserMapper, LdapUser> {

    /**
     * 查询用户（账号）
     */
    public LdapUser getByAccount(String tenantId, String userAccount){
        return lambdaQuery().eq(LdapUser::getTenantId, tenantId).eq(LdapUser::getUserAccount, userAccount).one();
    }

    /**
     * 查询用户（编码）
     */
    public LdapUser getByUserCode(String userCode){
        return lambdaQuery().eq(LdapUser::getUserCode, userCode).one();
    }

    /**
     * 删除用户
     */
    public void removeById(String tenantId, Integer userId){
        lambdaUpdate().eq(LdapUser::getTenantId, tenantId).eq(LdapUser::getId, userId).remove();
    }

    /**
     * 修改用户橘色
     */
    public void updateRoleCodeById(String tenantId, Integer userId, String roleCode){
        lambdaUpdate()
                .eq(LdapUser::getTenantId, tenantId)
                .eq(LdapUser::getId, userId)
                .set(LdapUser::getRoleCode, roleCode)
                .set(LdapUser::getUpdateTime, new Date())
                .update();
    }

    /**
     * 查询用户名称（编码）
     */
    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(LdapUser::getUserCode, userCode)
                .select(LdapUser::getUserName)
                .oneOpt().map(LdapUser::getUserName).orElse(null);
    }

    /**
     * 查询用户名称（编码）
     */
    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<LdapUser> list = lambdaQuery()
                .in(LdapUser::getUserCode, userCodes)
                .select(LdapUser::getUserCode, LdapUser::getUserName)
                .list();
        return Collections.copyToMap(list, LdapUser::getUserCode, LdapUser::getUserName);
    }
}
