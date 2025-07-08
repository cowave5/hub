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
import com.cowave.sys.admin.domain.base.SysNotice;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.TenantQuery;
import com.cowave.sys.admin.domain.rabc.request.TenantStatusUpdate;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysTenantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class SysTenantDao extends ServiceImpl<SysTenantMapper, SysTenant> {

    private static final String FORMAT = "%s-%s-%s";

    /**
     * 下一个用户编码
     */
    public String nextUserCode(String tenantId, String userType) {
        Integer userIndex = nextUserIndex(tenantId);
        return FORMAT.formatted(tenantId, userType, String.format("%05d", userIndex));
    }

    /**
     * 下一个用户序号
     */
    public Integer nextUserIndex(String tenantId) {
        while (true) {
            Integer currentIndex = lambdaQuery().eq(SysTenant::getTenantId, tenantId)
                    .oneOpt().map(SysTenant::getUserIndex).orElse(null);
            if (currentIndex == null) {
                return 1;
            }

            boolean updated = lambdaUpdate()
                    .eq(SysTenant::getTenantId, tenantId)
                    .eq(SysTenant::getUserIndex, currentIndex)
                    .setSql("user_index = user_index + 1")
                    .update();
            if (updated) {
                return currentIndex + 1;
            }
        }
    }

    /**
     * 分页查询
     */
    public Page<SysTenant> page(TenantQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getTenantId()), SysTenant::getTenantId, query.getTenantId())
                .orderByAsc(SysTenant::getCreateTime)
                .page(Access.page());
    }

    /**
     * 查询名称（id）
     */
    public String queryNameById(String tenantId) {
        return lambdaQuery()
                .eq(SysTenant::getTenantId, tenantId)
                .select(SysTenant::getTenantName)
                .oneOpt().map(SysTenant::getTenantName).orElse(null);
    }

    /**
     * 修改租户
     */
    public void updateTenant(SysTenant sysTenant){
        lambdaUpdate()
                .eq(SysTenant::getTenantId, sysTenant.getTenantId())
                .set(SysTenant::getTenantName, sysTenant.getTenantName())
                .set(SysTenant::getTenantUser, sysTenant.getTenantUser())
                .set(SysTenant::getTenantEmail, sysTenant.getTenantEmail())
                .set(SysTenant::getTenantPhone, sysTenant.getTenantPhone())
                .set(SysTenant::getTenantAddr, sysTenant.getTenantAddr())
                .set(SysTenant::getUserLimit, sysTenant.getUserLimit())
                .set(SysTenant::getTitle, sysTenant.getTitle())
                .set(SysTenant::getLogo, sysTenant.getLogo())
                .set(SysTenant::getUpdateBy, sysTenant.getUpdateBy())
                .set(SysTenant::getUpdateTime, sysTenant.getUpdateTime())
                .update();
    }

    /**
     * 修改状态
     */
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        lambdaUpdate().eq(SysTenant::getTenantId, statusUpdate.getTenantId())
                .set(SysTenant::getStatus, statusUpdate.getStatus())
                .set(SysTenant::getUpdateBy, Access.userCode())
                .set(SysTenant::getUpdateTime, new Date())
                .update();
    }
}
