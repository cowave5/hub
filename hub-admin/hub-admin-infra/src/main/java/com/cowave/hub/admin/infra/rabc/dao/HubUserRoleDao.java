/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rabc.HubUserRole;
import com.cowave.hub.admin.infra.rabc.mapper.HubUserRoleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubUserRoleDao extends ServiceImpl<HubUserRoleMapper, HubUserRole> {

    /**
     * 删除用户角色（用户id）
     */
    public void removeByUserId(Integer userId){
        lambdaUpdate().eq(HubUserRole::getUserId, userId).remove();
    }

    /**
     * 删除用户角色（角色id）
     */
    public void deleteByRoleIds(List<Integer> roleIds){
        lambdaUpdate().in(HubUserRole::getRoleId, roleIds).remove();
    }

    /**
     * 从角色中移除用户
     */
    public void deleteRoleUsers(Integer roleId, List<Integer> userIds){
        lambdaUpdate()
                .eq(HubUserRole::getRoleId, roleId)
                .in(HubUserRole::getUserId, userIds)
                .remove();
    }
}
