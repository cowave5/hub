/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.request.NoticeQuery;
import com.cowave.sys.admin.infra.base.dao.mapper.SysNoticeMapper;
import com.cowave.sys.admin.domain.base.SysNotice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class SysNoticeDao extends ServiceImpl<SysNoticeMapper, SysNotice> {

    public Page<SysNotice> queryPageOfUser(NoticeQuery query){
        return lambdaQuery()
                .eq(!Access.isAdminUser(), SysNotice::getCreateBy, Access.userCode())
                .eq(query.getNoticeType() != null, SysNotice::getNoticeType, query.getNoticeType())
                .eq(query.getNoticeStatus() != null, SysNotice::getNoticeStatus, query.getNoticeStatus())
                .like(StringUtils.isNotBlank(query.getNoticeTitle()), SysNotice::getNoticeTitle, query.getNoticeTitle())
                .orderByDesc(SysNotice::getCreateTime)
                .page(Access.page());
    }

    public void updateStatus(Long noticeId, Integer noticeStatus){
        lambdaUpdate().eq(SysNotice::getNoticeId, noticeId).set(SysNotice::getNoticeStatus, noticeStatus).update();
    }

    public void updateNotice(SysNotice sysNotice){
        lambdaUpdate().eq(SysNotice::getNoticeId, sysNotice.getNoticeId())
                .set(SysNotice::getUpdateBy, Access.userCode())
                .set(SysNotice::getUpdateTime, new Date())
                .set(SysNotice::getNoticeTitle, sysNotice.getNoticeTitle())
                .set(SysNotice::getNoticeType, sysNotice.getNoticeType())
                .set(SysNotice::getNoticeLevel, sysNotice.getNoticeLevel())
                .set(SysNotice::getContent, sysNotice.getContent())
                .set(SysNotice::getGoalsAll, sysNotice.getGoalsAll())
                .set(SysNotice::getGoalsDept, sysNotice.getGoalsDept())
                .set(SysNotice::getGoalsRole, sysNotice.getGoalsRole())
                .set(SysNotice::getGoalsUser, sysNotice.getGoalsUser())
                .update();
    }
}
