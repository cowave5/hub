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
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.request.TenantQuery;
import com.cowave.hub.admin.domain.rabc.request.TenantStatusUpdate;
import com.cowave.hub.admin.infra.rabc.mapper.HubTenantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class HubTenantDao extends ServiceImpl<HubTenantMapper, HubTenant> {

    /**
     * 分页查询
     */
    public Page<HubTenant> page(TenantQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getTenantId()), HubTenant::getTenantId, query.getTenantId())
                .orderByAsc(HubTenant::getCreateTime)
                .page(Access.page());
    }

    /**
     * 查询名称（id）
     */
    public String queryNameById(String tenantId) {
        return lambdaQuery()
                .eq(HubTenant::getTenantId, tenantId)
                .select(HubTenant::getTenantName)
                .oneOpt().map(HubTenant::getTenantName).orElse(null);
    }

    /**
     * 修改租户
     */
    public void updateTenant(HubTenant hubTenant){
        lambdaUpdate()
                .eq(HubTenant::getTenantId, hubTenant.getTenantId())
                .set(HubTenant::getTenantName, hubTenant.getTenantName())
                .set(HubTenant::getTenantUser, hubTenant.getTenantUser())
                .set(HubTenant::getTenantEmail, hubTenant.getTenantEmail())
                .set(HubTenant::getTenantPhone, hubTenant.getTenantPhone())
                .set(HubTenant::getTenantAddr, hubTenant.getTenantAddr())
                .set(HubTenant::getUserLimit, hubTenant.getUserLimit())
                .set(HubTenant::getTitle, hubTenant.getTitle())
                .set(HubTenant::getLogo, hubTenant.getLogo())
                .set(HubTenant::getUpdateBy, hubTenant.getUpdateBy())
                .set(HubTenant::getUpdateTime, hubTenant.getUpdateTime())
                .update();
    }

    /**
     * 修改状态
     */
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        lambdaUpdate().eq(HubTenant::getTenantId, statusUpdate.getTenantId())
                .set(HubTenant::getStatus, statusUpdate.getStatus())
                .set(HubTenant::getUpdateBy, Access.userCode())
                .set(HubTenant::getUpdateTime, new Date())
                .update();
    }
}
