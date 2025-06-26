/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysRole;
import com.cowave.sys.admin.domain.rabc.request.RoleQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysRoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysRoleDao extends ServiceImpl<SysRoleMapper, SysRole> {

    /**
     * 角色查询（角色id）
     */
    public SysRole getById(String tenantId, Integer roleId) {
        return lambdaQuery()
                .eq(SysRole::getTenantId, tenantId)
                .eq(SysRole::getRoleId, roleId)
                .one();
    }

    /**
     * 列表查询（角色id）
     */
    public List<SysRole> listByIds(String tenantId, List<Integer> roleIds) {
        return lambdaQuery()
                .eq(SysRole::getTenantId, tenantId)
                .in(SysRole::getRoleId, roleIds)
                .list();
    }

    /**
     * 分页查询
     */
    public Page<SysRole> queryPage(String tenantId, RoleQuery query) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .eq(StringUtils.isNotBlank(query.getRoleCode()), SysRole::getRoleCode, query.getRoleCode())
                .eq(StringUtils.isNotBlank(query.getRoleName()), SysRole::getRoleName, query.getRoleName())
                .page(Access.page());
    }

    /**
     * 查询角色id（角色编码）
     */
    public Integer queryIdByCode(String roleCode){
        return lambdaQuery()
                .select(SysRole::getRoleId)
                .eq(SysRole::getRoleCode, roleCode)
                .one().getRoleId();
    }

    /**
     * 冲突检测（角色编码）
     */
    public long countRoleCode(String tenantId, String roleCode, Integer roleId) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .eq(SysRole::getRoleCode, roleCode)
                .ne(roleId != null, SysRole::getRoleId, roleId)
                .count();
    }

    /**
     * 更新角色
     */
    public void updateRole(SysRole sysRole){
        lambdaUpdate()
                .eq(SysRole::getRoleId, sysRole.getRoleId())
                .set(SysRole::getUpdateBy, Access.userCode())
                .set(SysRole::getUpdateTime, new Date())
                .set(SysRole::getRoleName, sysRole.getRoleName())
                .set(SysRole::getRoleCode, sysRole.getRoleCode())
                .set(SysRole::getRoleType, sysRole.getRoleType())
                .set(SysRole::getRemark, sysRole.getRemark())
                .update();
    }

    /**
     * 角色名称查询（角色id）
     */
    public List<String> queryNameByIds(String tenantId, List<Integer> roleIds){
        if(roleIds.isEmpty()){
            return List.of();
        }
        List<SysRole> list = lambdaQuery()
                .eq(SysRole::getTenantId, tenantId)
                .in(SysRole::getRoleId, roleIds)
                .select(SysRole::getRoleName)
                .list();
        return Collections.copyToList(list, SysRole::getRoleName);
    }
}
