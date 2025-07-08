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
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.infra.base.dao.mapper.SysNoticeUserMapper;
import com.cowave.sys.admin.domain.base.SysNoticeUser;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.sys.admin.domain.base.constants.NoticeReadStatus.READ_PUBLISH;
import static com.cowave.sys.admin.domain.base.constants.NoticeReadStatus.UNREAD_PUBLISH;

/**
 * @author shanhuiming
 */
@Repository
public class SysNoticeUserDao extends ServiceImpl<SysNoticeUserMapper, SysNoticeUser> {

    public Page<SysNoticeUser> queryPageByNoticeId(Long noticeId){
        return lambdaQuery().eq(SysNoticeUser::getNoticeId, noticeId).page(Access.page());
    }

    public List<String> getUserCodesByNoticeId(Long noticeId) {
        List<SysNoticeUser> list = lambdaQuery()
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .select(SysNoticeUser::getUserCode).list();
        return Collections.copyToList(list, SysNoticeUser::getUserCode);
    }

    public Long countUnReadByUserId(String userCode) {
        return lambdaQuery()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getReadStatus, UNREAD_PUBLISH.val())
                .count();
    }

    public boolean updateReadStatus(String userCode, Long noticeId, Date readTime) {
        return lambdaUpdate()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .eq(SysNoticeUser::getReadStatus, UNREAD_PUBLISH.val())
                .set(SysNoticeUser::getReadStatus, READ_PUBLISH.val())
                .set(SysNoticeUser::getReadTime, readTime)
                .update();
    }

    public void updateReadBack(String userCode, Long noticeId, String readBack) {
        lambdaUpdate()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .set(SysNoticeUser::getReadBack, readBack)
                .update();
    }

    public void removeByNoticeId(Long noticeId) {
        lambdaUpdate().eq(SysNoticeUser::getNoticeId, noticeId).remove();
    }
}
