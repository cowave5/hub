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
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.base.vo.SelectOption;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import com.cowave.sys.admin.domain.rabc.SysUserTree;
import com.cowave.sys.admin.domain.rabc.dto.TenantManager;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserRoleDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserTreeDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.service.rabc.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.sys.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.sys.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.sys.admin.domain.constants.AuthType.SYS;
import static com.cowave.sys.admin.domain.constants.AttachType.LOGO;
import static com.cowave.sys.admin.domain.constants.OpModule.SYSTEM_TENANT;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantDao sysTenantDao;
    private final SysAttachDao sysAttachDao;
    private final SysUserDao sysUserDao;
    private final SysUserRoleDao sysUserRoleDao;
    private final SysUserTreeDao sysUserTreeDao;
    private final SysUserDtoMapper sysUserDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<SysTenant> page(TenantQuery query) {
        return sysTenantDao.page(query);
    }

    @Override
    public SysTenant info(String tenantId) {
        return sysTenantDao.getById(tenantId);
    }

    @Override
    public void create(TenantCreate tenantCreate) {
        sysTenantDao.save(tenantCreate);
        sysAttachDao.updateOwnerById(tenantCreate.getTenantId(), tenantCreate.getAttachId());
    }

    @Override
    public void edit(TenantCreate tenantCreate) {
        sysAttachDao.clearOwner(tenantCreate.getTenantId(), SYSTEM_TENANT, LOGO, tenantCreate.getAttachId());
        if (tenantCreate.getAttachId() != null) {
            sysAttachDao.updateOwnerById(tenantCreate.getTenantId(), tenantCreate.getAttachId());
        } else {
            tenantCreate.setLogo(null);
        }
        sysTenantDao.updateTenant(tenantCreate);
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        sysTenantDao.updateStatus(statusUpdate);
    }

    @Override
    public Page<TenantManager> listManager(String tenantId) {
        return sysUserDtoMapper.listTenantManager(tenantId, Access.page());
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#managerCreate.tenantId")
    @Override
    public void createManager(TenantManagerCreate managerCreate) {
        long accountCount = sysUserDao.countByAccount(
                managerCreate.getTenantId(), managerCreate.getUserAccount(), null);
		HttpAsserts.isTrue(accountCount == 0,
                BAD_REQUEST, "{admin.user.account.conflict}", managerCreate.getUserAccount());

        SysUser sysUser = managerCreate.newSysUser();
        sysUser.setUserCode(sysTenantDao.nextUserCode(managerCreate.getTenantId(), SYS.getVal()));
        sysUser.setUserPasswd(passwordEncoder.encode(managerCreate.getUserPasswd()));
        sysUserDao.save(sysUser);
        // sysAdmin
        SysUserRole userRole = new SysUserRole(sysUser.getUserId(), 1);
        sysUserRoleDao.save(userRole);
        // 用户关系
        SysUserTree sysUserTree = new SysUserTree(sysUser.getUserId(), 0, managerCreate.getTenantId());
		sysUserTreeDao.save(sysUserTree);
    }

    @Override
    public void removeManager(TenantManagerRemove managerRemove) {
        sysUserDtoMapper.removeTenantManager(managerRemove);
    }

    @Override
    public List<SelectOption> tenantOptions() {
        List<SysTenant> tenantList = sysTenantDao.list();
        return Collections.copyToList(tenantList, t -> new SelectOption(t.getTenantId(), t.getTitle()));
    }
}
