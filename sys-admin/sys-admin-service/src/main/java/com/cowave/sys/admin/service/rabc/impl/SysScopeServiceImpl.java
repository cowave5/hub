/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.domain.rabc.request.ScopeInfoUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeNameUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeQuery;
import com.cowave.sys.admin.domain.rabc.request.ScopeStatusUpdate;
import com.cowave.sys.admin.infra.rabc.dao.SysScopeDao;
import com.cowave.sys.admin.service.rabc.SysScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysScopeServiceImpl implements SysScopeService {

    private final SysScopeDao sysScopeDao;

    @Override
    public Page<SysScope> page(String tenantId, ScopeQuery query) {
        return sysScopeDao.page(tenantId, query);
    }

    @Override
    public SysScope info(String tenantId, Integer scopeId) {
        return sysScopeDao.getById(tenantId, scopeId);
    }

    @Override
    public void create(String tenantId, SysScope sysScope) {
        sysScope.setTenantId(tenantId);
        sysScopeDao.save(sysScope);
    }

    @Override
    public void delete(String tenantId, List<Integer> scopeIds) {
        sysScopeDao.deleteByIds(tenantId, scopeIds);
    }

    @Override
    public void edit(String tenantId, ScopeNameUpdate nameUpdate) {
        sysScopeDao.updateNameById(tenantId, nameUpdate.getScopeId(), nameUpdate.getScopeName());
    }

    @Override
    public void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate) {
        sysScopeDao.updateStatusById(tenantId, statusUpdate.getScopeId(), statusUpdate.getScopeStatus());
    }

    @Override
    public void editContent(String tenantId, ScopeInfoUpdate infoUpdate) {
        sysScopeDao.updateContentById(tenantId, infoUpdate.getScopeId(), infoUpdate.getScopeContent());
    }
}
