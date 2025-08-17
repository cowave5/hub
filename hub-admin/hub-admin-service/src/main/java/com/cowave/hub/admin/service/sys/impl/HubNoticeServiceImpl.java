/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.sys.HubAttach;
import com.cowave.hub.admin.domain.sys.HubNotice;
import com.cowave.hub.admin.domain.sys.HubNoticeUser;
import com.cowave.hub.admin.domain.sys.dto.NoticeDto;
import com.cowave.hub.admin.domain.sys.dto.NoticeMsgDto;
import com.cowave.hub.admin.domain.sys.dto.NoticeUserDto;
import com.cowave.hub.admin.domain.sys.request.AttachView;
import com.cowave.hub.admin.domain.sys.request.NoticeCreate;
import com.cowave.hub.admin.domain.sys.request.NoticeMsgBack;
import com.cowave.hub.admin.domain.sys.request.NoticeQuery;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.infra.sys.dao.HubAttachDao;
import com.cowave.hub.admin.infra.sys.dao.HubNoticeDao;
import com.cowave.hub.admin.infra.sys.dao.HubNoticeUserDao;
import com.cowave.hub.admin.infra.sys.mapper.dto.HubNoticeDtoMapper;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.service.sys.HubAttachService;
import com.cowave.hub.admin.service.sys.HubNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.sys.enums.NoticeLevel.COMMON;
import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.*;
import static com.cowave.hub.admin.domain.sys.enums.NoticeType.PRESS;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_NOTICE;
import static com.cowave.hub.admin.domain.rabc.enums.YesNo.NO;
import static com.cowave.hub.admin.domain.rabc.enums.YesNo.YES;
import static com.cowave.hub.admin.infra.AdminSocketIoConfiguration.EVENT_SERVER_NOTICE_NEW;
import static com.cowave.hub.admin.infra.AdminSocketIoConfiguration.SPACE_NOTICE;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubNoticeServiceImpl implements HubNoticeService {
    private final HubAttachService hubAttachService;
    private final SocketIoHelper socketIoHelper;
    private final HubAttachDao hubAttachDao;
    private final HubUserDao hubUserDao;
    private final HubNoticeDao hubNoticeDao;
    private final HubNoticeUserDao hubNoticeUserDao;
    private final HubNoticeDtoMapper hubNoticeDtoMapper;

    @Override
    public Response.Page<NoticeDto> list(String tenantId, NoticeQuery query) {
        Page<HubNotice> page = hubNoticeDao.pageOfUser(tenantId, query);

        Set<String> userCodes = Collections.copyToSet(page.getRecords(), HubNotice::getCreateBy);
        Map<String, String> codeNameMap = hubUserDao.queryCodeNameMap(userCodes);

        List<NoticeDto> dtoList = new ArrayList<>();
        for (HubNotice hubNotice : page.getRecords()) {
            String userName = codeNameMap.get(hubNotice.getCreateBy());
            NoticeDto noticeDto = BeanUtil.copyProperties(hubNotice, NoticeDto.class);
            noticeDto.setCreateUserName(userName);
            dtoList.add(noticeDto);
        }
        return new Response.Page<>(dtoList, page.getTotal());
    }

    @Override
    public NoticeDto info(String tenantId, Long noticeId) {
        HubNotice hubNotice = hubNoticeDao.getById(tenantId, noticeId);
        String userName = hubUserDao.queryNameByCode(hubNotice.getCreateBy());
        NoticeDto noticeDto = BeanUtil.copyProperties(hubNotice, NoticeDto.class);
        noticeDto.setCreateUserName(userName);
        return noticeDto;
    }

    @Override
    public void add(String tenantId, NoticeCreate notice) throws Exception {
        notice.setTenantId(tenantId);
        hubNoticeDao.save(notice);
        filterAttaches(notice);
    }

    @Override
    public void delete(String tenantId, List<Long> noticeIds) throws Exception {
        for (Long noticeId : noticeIds) {
            delete(tenantId, noticeId);
        }
    }

    private void delete(String tenantId, Long noticeId) throws Exception {
        HubNotice notice = hubNoticeDao.getById(tenantId, noticeId);
        if (notice == null) {
            return;
        }
        if (!Access.isAdminUser()) {
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.delete.self}");
        }

        List<HubAttach> attachList = hubAttachDao.listOfOwner(String.valueOf(noticeId), SYSTEM_NOTICE, null);
        NoticeStatus noticeStatus = notice.getNoticeStatus();
        if (DRAFT == noticeStatus) {
            // 草稿 -> 直接删除
            hubNoticeDao.removeById(noticeId);
            for (HubAttach attach : attachList) {
                hubAttachService.remove(attach);
            }
        } else if (PUBLISH == noticeStatus) {
            // 已发布 -> 改为撤回
            hubNoticeDao.updateStatus(noticeId, RECALL);
        } else if (RECALL == noticeStatus) {
            // 已撤回 -> 直接删除
            hubNoticeDao.removeById(noticeId);
            for (HubAttach attach : attachList) {
                hubAttachService.remove(attach);
            }
            hubNoticeUserDao.removeByNoticeId(noticeId);
        }
    }

    @Override
    public void edit(String tenantId, NoticeCreate notice) throws Exception {
        HttpAsserts.notNull(notice.getNoticeId(), BAD_REQUEST, "{admin.notice.id.null}");

        HubNotice preNotice = hubNoticeDao.getById(tenantId, notice.getNoticeId());
        HttpAsserts.notNull(preNotice, NOT_FOUND, "{admin.notice.not.exist}", notice.getNoticeId());
        HttpAsserts.equals(DRAFT, preNotice.getNoticeStatus(), BAD_REQUEST, "{admin.notice.edit.unpublish}");

        if (!Access.isAdminUser()) {
            HttpAsserts.equals(preNotice.getCreateBy(), FORBIDDEN, Access.userCode(), "{admin.notice.edit.self}");
        }
        hubNoticeDao.updateNotice(notice);
        filterAttaches(notice);
    }

    private void filterAttaches(NoticeCreate notice) throws Exception {
        String content = notice.getContent();
        // 删掉content中不存在的附件
        List<AttachView> attaches = notice.getAttaches();
        for (AttachView attach : attaches) {
            if (content.contains(attach.getAttachPath())) {
                hubAttachDao.updateOwnerById(String.valueOf(notice.getNoticeId()), attach.getAttachId());
            } else {
                hubAttachService.removeById(attach.getAttachId());
            }
        }
    }

    @Override
    public void publish(String tenantId, Long noticeId) {
        HubNotice notice = hubNoticeDao.getById(tenantId, noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", noticeId);
        if (!Access.isAdminUser()) {
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.publish.self}");
        }
        if (DRAFT != notice.getNoticeStatus()) {
            return;
        }

        // 转换read信息
        if (YES == notice.getGoalsAll()) {
            hubNoticeDtoMapper.insertReadOfAll(tenantId, noticeId);
        } else {
            hubNoticeDtoMapper.insertReadOfDept(tenantId, noticeId, notice.getGoalsDept());
            hubNoticeDtoMapper.insertReadOfRole(tenantId, noticeId, notice.getGoalsRole());
            hubNoticeDtoMapper.insertReadOfUser(tenantId, noticeId, notice.getGoalsUser());
        }
        hubNoticeDtoMapper.updateMsgStat(noticeId, PUBLISH, new Date());
        // 推送
        List<String> userCodes = hubNoticeUserDao.getUserCodesByNoticeId(noticeId);
        socketIoHelper.sendClientsOfNamespace(SPACE_NOTICE, userCodes, EVENT_SERVER_NOTICE_NEW, notice.getNoticeTitle());
    }

    @Override
    public Response.Page<NoticeUserDto> getNoticeReaders(String tenantId, Long noticeId) {
        HubNotice notice = hubNoticeDao.getById(tenantId, noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", noticeId);

        Page<HubNoticeUser> page = hubNoticeUserDao.queryPageByNoticeId(noticeId);
        Set<String> userCodes = Collections.copyToSet(page.getRecords(), HubNoticeUser::getUserCode);
        Map<String, String> codeNameMap = hubUserDao.queryCodeNameMap(userCodes);

        List<NoticeUserDto> dtoList = new ArrayList<>();
        for (HubNoticeUser noticeUser : page.getRecords()) {
            String userName = codeNameMap.get(noticeUser.getUserCode());
            NoticeUserDto noticeUserDto = BeanUtil.copyProperties(noticeUser, NoticeUserDto.class);
            noticeUserDto.setUserName(userName);
            dtoList.add(noticeUserDto);
        }
        return new Response.Page<>(dtoList, page.getTotal());
    }

    @Override
    public Page<NoticeMsgDto> msgList() {
        Page<NoticeMsgDto> page = hubNoticeDtoMapper.msgList(Access.page(), Access.userCode());
        Set<String> userCodes = Collections.copyToSet(page.getRecords(), NoticeMsgDto::getCreateBy);
        Map<String, String> codeNameMap = hubUserDao.queryCodeNameMap(userCodes);
        for (NoticeMsgDto msgDto : page.getRecords()) {
            msgDto.setCreateBy(codeNameMap.get(msgDto.getCreateBy()));
        }
        return page;
    }

    @Override
    public void msgRead(Long noticeId) {
        boolean statusUpdated = hubNoticeUserDao.updateReadStatus(Access.userCode(), noticeId, Access.accessTime());
        if (statusUpdated) {
            hubNoticeDtoMapper.updateReadStat(noticeId);
        }
    }

    @Override
    public void msgBack(NoticeMsgBack msgBack) {
        hubNoticeUserDao.updateReadBack(Access.userCode(), msgBack.getNoticeId(), msgBack.getReadBack());
    }

    @Override
    public void msgDelete(Long msgId) {
        hubNoticeDtoMapper.msgDelete(Access.userCode(), msgId);
    }

    @Override
    public long msgUnReadCount(String userCode) {
        return hubNoticeUserDao.countUnReadByUserId(userCode);
    }

    @Override
    public void sendUserNotice(HubNotice notice, Integer userId) {
        hubNoticeDao.save(notice);
//        hubNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(userId));
    }

    public void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser) {
        String startUserName = hubUserDao.queryNameById(startUser);
        HubNotice notice = new HubNotice();
        notice.setNoticeStatus(PUBLISH);
        notice.setCreateBy(Access.userCode());
        notice.setNoticeType(PRESS);
        notice.setNoticeLevel(COMMON);
        notice.setIsSystem(NO);
        notice.setStatTotal(1);
        notice.setStatRead(0);
        notice.setCreateTime(new Date());
        notice.setPublishTime(new Date());
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        hubNoticeDao.save(notice);
//        hubNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(assigneeUser));
//        socketIoHelper.sendSingle("flow_notice", "催办提醒: " + startUserName + "的" + processName + "[" + taskName + "]", assigneeUser);
    }
}
