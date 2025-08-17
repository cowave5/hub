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
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.sys.vo.SelectOption;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.domain.rabc.HubUserRole;
import com.cowave.hub.admin.domain.rabc.HubUserDiagram;
import com.cowave.hub.admin.domain.rabc.dto.TenantManager;
import com.cowave.hub.admin.domain.rabc.request.*;
import com.cowave.hub.admin.infra.sys.dao.HubAttachDao;
import com.cowave.hub.admin.infra.rabc.dao.HubTenantDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserRoleDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDiagramDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubUserDtoMapper;
import com.cowave.hub.admin.service.rabc.HubTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.hub.admin.domain.sys.enums.AttachType.LOGO;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_TENANT;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubTenantServiceImpl implements HubTenantService {
    private final HubTenantDao hubTenantDao;
    private final HubAttachDao hubAttachDao;
    private final HubUserDao hubUserDao;
    private final HubUserRoleDao hubUserRoleDao;
    private final HubUserDiagramDao hubUserDiagramDao;
    private final HubUserDtoMapper hubUserDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<HubTenant> page(TenantQuery query) {
        return hubTenantDao.page(query);
    }

    @Override
    public HubTenant info(String tenantId) {
        return hubTenantDao.getById(tenantId);
    }

    @Override
    public void create(TenantCreate tenantCreate) {
        hubTenantDao.save(tenantCreate);
        hubAttachDao.updateOwnerById(tenantCreate.getTenantId(), tenantCreate.getAttachId());
    }

    @Override
    public void edit(TenantCreate tenantCreate) {
        hubAttachDao.clearOwner(tenantCreate.getTenantId(), SYSTEM_TENANT, LOGO, tenantCreate.getAttachId());
        if (tenantCreate.getAttachId() != null) {
            hubAttachDao.updateOwnerById(tenantCreate.getTenantId(), tenantCreate.getAttachId());
        } else {
            tenantCreate.setLogo(null);
        }
        hubTenantDao.updateTenant(tenantCreate);
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        hubTenantDao.updateStatus(statusUpdate);
    }

    @Override
    public Page<TenantManager> listManager(String tenantId) {
        return hubUserDtoMapper.listTenantManager(tenantId, Access.page());
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#managerCreate.tenantId")
    @Override
    public void createManager(TenantManagerCreate managerCreate) {
        long accountCount = hubUserDao.countByAccount(
                managerCreate.getTenantId(), UserType.SYS, managerCreate.getUserAccount(), null);
		HttpAsserts.isTrue(accountCount == 0,
                BAD_REQUEST, "{admin.user.account.conflict}", managerCreate.getUserAccount());

        HubUser hubUser = managerCreate.newSysUser();
        hubUser.setUserPasswd(passwordEncoder.encode(managerCreate.getUserPasswd()));
        hubUserDao.save(hubUser);
        // admin user
        HubUserRole userRole = new HubUserRole(hubUser.getUserId(), 1);
        hubUserRoleDao.save(userRole);
        // 用户关系
        HubUserDiagram hubUserDiagram = new HubUserDiagram(hubUser.getUserId(), 0, managerCreate.getTenantId());
		hubUserDiagramDao.save(hubUserDiagram);
    }

    @Override
    public void removeManager(TenantManagerRemove managerRemove) {
        hubUserDtoMapper.removeTenantManager(managerRemove);
    }

    @Override
    public List<SelectOption> tenantOptions() {
        List<HubTenant> tenantList = hubTenantDao.list();
        return Collections.copyToList(tenantList, t -> new SelectOption(t.getTenantId(), t.getTitle()));
    }
}
