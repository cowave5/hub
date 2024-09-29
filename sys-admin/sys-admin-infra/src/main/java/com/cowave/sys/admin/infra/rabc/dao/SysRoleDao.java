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
import com.cowave.sys.admin.domain.rabc.SysUser;
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

    public String queryCodeById(Integer roleId) {
        return lambdaQuery().select(SysRole::getRoleCode)
                .eq(SysRole::getRoleId, roleId)
                .one().getRoleCode();
    }

    public Integer queryIdByCode(String roleCode){
        return lambdaQuery().select(SysRole::getRoleId)
                .eq(SysRole::getRoleCode, roleCode)
                .one().getRoleId();
    }

    public Page<SysRole> queryPage(RoleQuery query) {
        return lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getRoleCode()), SysRole::getRoleCode, query.getRoleCode())
                .eq(StringUtils.isNotBlank(query.getRoleName()), SysRole::getRoleName, query.getRoleName())
                .page(Access.page());
    }

    public long countRoleCode(String roleCode, Integer roleId) {
        return lambdaQuery().eq(SysRole::getRoleCode, roleCode)
                .ne(roleId != null, SysRole::getRoleId, roleId).count();
    }

    public long countRoleName(String roleName, Integer roleId) {
        return lambdaQuery().eq(SysRole::getRoleName, roleName)
                .ne(roleId != null, SysRole::getRoleId, roleId).count();
    }

    public void updateRole(SysRole sysRole){
        lambdaUpdate().eq(SysRole::getRoleId, sysRole.getRoleId())
                .set(SysRole::getUpdateBy, Access.userCode())
                .set(SysRole::getUpdateTime, new Date())
                .set(SysRole::getRoleName, sysRole.getRoleName())
                .set(SysRole::getRoleCode, sysRole.getRoleCode())
                .set(SysRole::getRoleType, sysRole.getRoleType())
                .set(SysRole::getRemark, sysRole.getRemark())
                .update();
    }

    public List<String> queryNameByIds(List<Integer> roleIds){
        if(roleIds.isEmpty()){
            return List.of();
        }
        List<SysRole> list = lambdaQuery()
                .in(SysRole::getRoleId, roleIds)
                .select(SysRole::getRoleName)
                .list();
        return Collections.copyToList(list, SysRole::getRoleName);
    }
}
