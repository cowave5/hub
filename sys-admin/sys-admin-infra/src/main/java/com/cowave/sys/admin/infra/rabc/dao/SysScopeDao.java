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
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.domain.rabc.request.ScopeQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysScopeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class SysScopeDao extends ServiceImpl<SysScopeMapper, SysScope> {

    /**
     * 分页查询
     */
    public Page<SysScope> page(String tenantId, ScopeQuery query) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getScopeModule()), SysScope::getScopeModule, query.getScopeModule())
                .page(Access.page());
    }

    /**
     * 查询（id）
     */
    public SysScope getById(String tenantId, Integer scopeId) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .one();
    }

    /**
     * 删除
     */
    public void deleteByIds(String tenantId, List<Integer> scopeIds) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .in(SysScope::getScopeId, scopeIds)
                .remove();
    }

    /**
     * 修改权限名称
     */
    public void updateNameById(String tenantId, Integer scopeId, String scopeName) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeName, scopeName)
                .update();
    }

    /**
     * 修改权限状态
     */
    public void updateStatusById(String tenantId, Integer scopeId, Integer status){
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeStatus, status)
                .update();
    }

    /**
     * 修改权限内容
     */
    public void updateContentById(String tenantId, Integer scopeId, Map<String, Object> content){
        SysScope sysScope = new SysScope(); // 为了生效字段上的注解
        sysScope.setScopeContent(content);
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .update(sysScope);
    }
}
