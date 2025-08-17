/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rabc.HubScope;
import com.cowave.hub.admin.domain.rabc.request.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rabc.request.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rabc.request.ScopeQuery;
import com.cowave.hub.admin.domain.rabc.request.ScopeStatusUpdate;
import com.cowave.hub.admin.infra.rabc.dao.HubScopeDao;
import com.cowave.hub.admin.service.rabc.HubScopeService;
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
public class HubScopeServiceImpl implements HubScopeService {

    private final HubScopeDao hubScopeDao;

    @Override
    public Page<HubScope> page(String tenantId, ScopeQuery query) {
        return hubScopeDao.page(tenantId, query);
    }

    @Override
    public HubScope info(String tenantId, Integer scopeId) {
        return hubScopeDao.getById(tenantId, scopeId);
    }

    @Override
    public void create(String tenantId, HubScope hubScope) {
        hubScope.setTenantId(tenantId);
        hubScopeDao.save(hubScope);
    }

    @Override
    public void delete(String tenantId, List<Integer> scopeIds) {
        hubScopeDao.deleteByIds(tenantId, scopeIds);
    }

    @Override
    public void edit(String tenantId, ScopeNameUpdate nameUpdate) {
        hubScopeDao.updateNameById(tenantId, nameUpdate.getScopeId(), nameUpdate.getScopeName());
    }

    @Override
    public void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate) {
        hubScopeDao.updateStatusById(tenantId, statusUpdate.getScopeId(), statusUpdate.getScopeStatus());
    }

    @Override
    public void editContent(String tenantId, ScopeInfoUpdate infoUpdate) {
        hubScopeDao.updateContentById(tenantId, infoUpdate.getScopeId(), infoUpdate.getScopeContent());
    }
}
