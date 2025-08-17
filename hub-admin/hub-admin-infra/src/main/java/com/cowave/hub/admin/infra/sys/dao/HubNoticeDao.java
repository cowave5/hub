/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.request.NoticeQuery;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeMapper;
import com.cowave.hub.admin.domain.sys.HubNotice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class HubNoticeDao extends ServiceImpl<HubNoticeMapper, HubNotice> {

    /**
     * 分页查询（用户）
     */
    public Page<HubNotice> pageOfUser(String tenantId, NoticeQuery query) {
        return lambdaQuery()
                .eq(HubNotice::getTenantId, tenantId)
                .eq(!Access.isAdminUser(), HubNotice::getCreateBy, Access.userCode())
                .eq(query.getNoticeType() != null, HubNotice::getNoticeType, query.getNoticeType())
                .eq(query.getNoticeStatus() != null, HubNotice::getNoticeStatus, query.getNoticeStatus())
                .like(StringUtils.isNotBlank(query.getNoticeTitle()), HubNotice::getNoticeTitle, query.getNoticeTitle())
                .orderByDesc(HubNotice::getCreateTime)
                .page(Access.page());
    }

    /**
     * 查询（id）
     */
    public HubNotice getById(String tenantId, Long noticeId) {
        return lambdaQuery()
                .eq(HubNotice::getTenantId, tenantId)
                .eq(HubNotice::getNoticeId, noticeId)
                .one();
    }

    /**
     * 查询名称（id）
     */
    public String queryNameById(Long noticeId) {
        return lambdaQuery()
                .eq(HubNotice::getNoticeId, noticeId)
                .select(HubNotice::getNoticeTitle)
                .oneOpt().map(HubNotice::getNoticeTitle).orElse(null);
    }

    /**
     * 更新状态
     */
    public void updateStatus(Long noticeId, NoticeStatus noticeStatus) {
        lambdaUpdate().eq(HubNotice::getNoticeId, noticeId).set(HubNotice::getNoticeStatus, noticeStatus).update();
    }

    /**
     * 更新信息
     */
    public void updateNotice(HubNotice hubNotice) {
        lambdaUpdate().eq(HubNotice::getNoticeId, hubNotice.getNoticeId())
                .set(HubNotice::getUpdateBy, Access.userCode())
                .set(HubNotice::getUpdateTime, new Date())
                .set(HubNotice::getNoticeTitle, hubNotice.getNoticeTitle())
                .set(HubNotice::getNoticeType, hubNotice.getNoticeType())
                .set(HubNotice::getNoticeLevel, hubNotice.getNoticeLevel())
                .set(HubNotice::getContent, hubNotice.getContent())
                .set(HubNotice::getGoalsAll, hubNotice.getGoalsAll())
                .set(HubNotice::getGoalsDept, hubNotice.getGoalsDept())
                .set(HubNotice::getGoalsRole, hubNotice.getGoalsRole())
                .set(HubNotice::getGoalsUser, hubNotice.getGoalsUser())
                .update();
    }
}
