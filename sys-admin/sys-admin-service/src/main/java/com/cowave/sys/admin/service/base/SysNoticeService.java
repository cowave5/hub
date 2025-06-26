/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.SysNotice;
import com.cowave.sys.admin.domain.base.dto.*;
import com.cowave.sys.admin.domain.base.request.NoticeCreate;
import com.cowave.sys.admin.domain.base.request.NoticeMsgBack;
import com.cowave.sys.admin.domain.base.request.NoticeQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysNoticeService {

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
    void add(NoticeCreate sysNotice) throws Exception;

    /**
     * 删除
     */
    void delete(String tenantId, List<Long> noticeIds) throws Exception;

    /**
     * 修改
     */
    void edit(String tenantId, NoticeCreate sysNotice) throws Exception;

    /**
     * 发布
     */
    void publish(String tenantId, Long noticeId);

    /**
     * 上传图片
     */
    SysAttach imageUpload(String tenantId, MultipartFile file) throws Exception;

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
    void sendUserNotice(SysNotice notice, Integer userId);

    /**
     * 流程通知
     */
    void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser);
}
