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
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeUserMapper;
import com.cowave.hub.admin.domain.sys.HubNoticeUser;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.READ_PUBLISH;
import static com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.UNREAD_PUBLISH;

/**
 * @author shanhuiming
 */
@Repository
public class HubNoticeUserDao extends ServiceImpl<HubNoticeUserMapper, HubNoticeUser> {

    public Page<HubNoticeUser> queryPageByNoticeId(Long noticeId){
        return lambdaQuery().eq(HubNoticeUser::getNoticeId, noticeId).page(Access.page());
    }

    public List<String> getUserCodesByNoticeId(Long noticeId) {
        List<HubNoticeUser> list = lambdaQuery()
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .select(HubNoticeUser::getUserCode).list();
        return Collections.copyToList(list, HubNoticeUser::getUserCode);
    }

    public Long countUnReadByUserId(String userCode) {
        return lambdaQuery()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getReadStatus, UNREAD_PUBLISH)
                .count();
    }

    public boolean updateReadStatus(String userCode, Long noticeId, Date readTime) {
        return lambdaUpdate()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .eq(HubNoticeUser::getReadStatus, UNREAD_PUBLISH)
                .set(HubNoticeUser::getReadStatus, READ_PUBLISH)
                .set(HubNoticeUser::getReadTime, readTime)
                .update();
    }

    public void updateReadBack(String userCode, Long noticeId, String readBack) {
        lambdaUpdate()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .set(HubNoticeUser::getReadBack, readBack)
                .update();
    }

    public void removeByNoticeId(Long noticeId) {
        lambdaUpdate().eq(HubNoticeUser::getNoticeId, noticeId).remove();
    }
}
