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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.rabc.HubRole;
import com.cowave.hub.admin.domain.rabc.request.RoleQuery;
import com.cowave.hub.admin.infra.rabc.mapper.HubRoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubRoleDao extends ServiceImpl<HubRoleMapper, HubRole> {

    /**
     * 角色查询（角色id）
     */
    public HubRole getById(String tenantId, Integer roleId) {
        return lambdaQuery()
                .eq(HubRole::getTenantId, tenantId)
                .eq(HubRole::getRoleId, roleId)
                .one();
    }

    /**
     * 查询角色id（角色编码）
     */
    public HubRole getByCode(String tenantId, String roleCode){
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(HubRole::getRoleCode, roleCode)
                .one();
    }

    /**
     * 列表查询（角色id）
     */
    public List<HubRole> listByIds(String tenantId, List<Integer> roleIds) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .in(HubRole::getRoleId, roleIds)
                .list();
    }

    /**
     * 分页查询
     */
    public Page<HubRole> queryPage(String tenantId, RoleQuery query) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(StringUtils.isNotBlank(query.getRoleCode()), HubRole::getRoleCode, query.getRoleCode())
                .eq(StringUtils.isNotBlank(query.getRoleName()), HubRole::getRoleName, query.getRoleName())
                .page(Access.page());
    }

    /**
     * 冲突检测（角色编码）
     */
    public long countRoleCode(String tenantId, String roleCode, Integer roleId) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(HubRole::getRoleCode, roleCode)
                .ne(roleId != null, HubRole::getRoleId, roleId)
                .count();
    }

    /**
     * 更新角色
     */
    public void updateRole(HubRole hubRole){
        lambdaUpdate()
                .eq(HubRole::getRoleId, hubRole.getRoleId())
                .set(HubRole::getUpdateBy, Access.userCode())
                .set(HubRole::getUpdateTime, new Date())
                .set(HubRole::getRoleName, hubRole.getRoleName())
                .set(HubRole::getRoleCode, hubRole.getRoleCode())
                .set(HubRole::getRoleType, hubRole.getRoleType())
                .set(HubRole::getRemark, hubRole.getRemark())
                .update();
    }

    /**
     * 角色名称查询（角色id）
     */
    public List<String> queryNameByIds(String tenantId, List<Integer> roleIds){
        if(roleIds.isEmpty()){
            return List.of();
        }
        List<HubRole> list = lambdaQuery()
                .eq(HubRole::getTenantId, tenantId)
                .in(HubRole::getRoleId, roleIds)
                .select(HubRole::getRoleName)
                .list();
        return Collections.copyToList(list, HubRole::getRoleName);
    }
}
