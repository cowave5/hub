/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.response.Response;
import com.cowave.hub.admin.domain.sys.HubNotice;
import com.cowave.hub.admin.domain.sys.dto.NoticeDto;
import com.cowave.hub.admin.domain.sys.dto.NoticeMsgDto;
import com.cowave.hub.admin.domain.sys.dto.NoticeUserDto;
import com.cowave.hub.admin.domain.sys.request.NoticeCreate;
import com.cowave.hub.admin.domain.sys.request.NoticeMsgBack;
import com.cowave.hub.admin.domain.sys.request.NoticeQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubNoticeService {

    /**
     * 列表
     */
    Response.Page<NoticeDto> list(String tenantId, NoticeQuery query);

    /**
     * 详情
     */
    NoticeDto info(String tenantId, Long noticeId);

    /**
     * 新增
     */
    void add(String tenantId, NoticeCreate notice) throws Exception;

    /**
     * 删除
     */
    void delete(String tenantId, List<Long> noticeIds) throws Exception;

    /**
     * 修改
     */
    void edit(String tenantId, NoticeCreate notice) throws Exception;

    /**
     * 发布
     */
    void publish(String tenantId, Long noticeId);

    /**
     * 已读列表
     */
    Response.Page<NoticeUserDto> getNoticeReaders(String tenantId, Long noticeId);

    /**
     * 消息列表
     */
    Page<NoticeMsgDto> msgList();

    /**
     * 阅读消息
     */
    void msgRead(Long noticeId);

    /**
     * 反馈消息
     */
    void msgBack(NoticeMsgBack msgBack);

    /**
     * 删除消息
     */
    void msgDelete(Long msgId);

    /**
     * 未读消息计数
     */
    long msgUnReadCount(String userCode);

    /**
     * 发送通知
     */
    void sendUserNotice(HubNotice notice, Integer userId);

    /**
     * 流程通知
     */
    void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser);
}
