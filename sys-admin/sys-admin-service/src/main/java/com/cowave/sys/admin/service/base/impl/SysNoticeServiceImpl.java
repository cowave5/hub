/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.SysNotice;
import com.cowave.sys.admin.domain.base.SysNoticeUser;
import com.cowave.sys.admin.domain.base.dto.*;
import com.cowave.sys.admin.domain.base.request.*;
import com.cowave.sys.admin.domain.constants.NoticeStatus;
import com.cowave.sys.admin.infra.auth.dao.LdapUserDao;
import com.cowave.sys.admin.infra.auth.dao.OAuthUserDao;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.infra.base.dao.SysNoticeDao;
import com.cowave.sys.admin.infra.base.dao.SysNoticeUserDao;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysNoticeDtoMapper;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.service.base.SysAttachService;
import com.cowave.sys.admin.service.base.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.constants.NoticeLevel.COMMON;
import static com.cowave.sys.admin.domain.constants.NoticeStatus.*;
import static com.cowave.sys.admin.domain.constants.AuthType.*;
import static com.cowave.sys.admin.domain.constants.NoticeType.PRESS;
import static com.cowave.sys.admin.domain.constants.OpModule.SYSTEM_NOTICE;
import static com.cowave.sys.admin.domain.constants.YesNo.NO;
import static com.cowave.sys.admin.domain.constants.YesNo.YES;
import static com.cowave.sys.admin.infra.base.socketio.SysSocketConfiguration.EVENT_SERVER_NOTICE_NEW;
import static com.cowave.sys.admin.infra.base.socketio.SysSocketConfiguration.SPACE_NOTICE;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysNoticeServiceImpl implements SysNoticeService {
    private final SysAttachService sysAttachService;
    private final SocketIoHelper socketIoHelper;
    private final SysAttachDao sysAttachDao;
    private final SysUserDao sysUserDao;
    private final LdapUserDao ldapUserDao;
    private final OAuthUserDao oauthUserDao;
    private final SysNoticeDao sysNoticeDao;
    private final SysNoticeUserDao sysNoticeUserDao;
    private final SysNoticeDtoMapper sysNoticeDtoMapper;

    @Override
    public Response.Page<NoticeDto> list(String tenantId, NoticeQuery query) {
        Page<SysNotice> page = sysNoticeDao.pageOfUser(tenantId, query);
        List<NoticeDto> dtoList = new ArrayList<>();

        List<String> sysCodes = new ArrayList<>();
        List<String> ldapCodes = new ArrayList<>();
        List<String> gitlabCodes = new ArrayList<>();
        for (SysNotice sysNotice : page.getRecords()) {
            dtoList.add(BeanUtil.copyProperties(sysNotice, NoticeDto.class));
            String userCode = sysNotice.getCreateBy();
            if (SYS.equalsType(userCode)) {
                sysCodes.add(userCode);
            } else if (LDAP.equalsType(userCode)) {
                ldapCodes.add(userCode);
            } else if (GITLAB.equalsType(userCode)) {
                gitlabCodes.add(userCode);
            }
        }
        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for (NoticeDto dto : dtoList) {
            String userName = sysMap.get(dto.getCreateBy());
            dto.setCreateUserName(userName);
        }
        return new Response.Page<>(dtoList, page.getTotal());
    }

    @Override
    public NoticeDto info(String tenantId, Long noticeId) {
        SysNotice sysNotice = sysNoticeDao.getById(tenantId, noticeId);
        NoticeDto infoDto = BeanUtil.copyProperties(sysNotice, NoticeDto.class);

        String userCode = infoDto.getCreateBy();
        if (SYS.equalsType(userCode)) {
            infoDto.setCreateUserName(sysUserDao.queryNameByCode(userCode));
        } else if (LDAP.equalsType(userCode)) {
            infoDto.setCreateUserName(ldapUserDao.queryNameByCode(userCode));
        } else if (GITLAB.equalsType(userCode)) {
            infoDto.setCreateUserName(oauthUserDao.queryNameByCode(userCode));
        }
        return infoDto;
    }

    @Override
    public void add(String tenantId, NoticeCreate sysNotice) throws Exception {
        sysNotice.setTenantId(tenantId);
        sysNoticeDao.save(sysNotice);
        filterAttaches(sysNotice);
    }

    @Override
    public void delete(String tenantId, List<Long> noticeIds) throws Exception {
        for (Long noticeId : noticeIds) {
            delete(tenantId, noticeId);
        }
    }

    private void delete(String tenantId, Long noticeId) throws Exception {
        SysNotice notice = sysNoticeDao.getById(tenantId, noticeId);
        if (notice == null) {
            return;
        }
        if (!Access.isAdminUser()) {
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.delete.self}");
        }

        List<SysAttach> attachList = sysAttachDao.listOfOwner(String.valueOf(noticeId), SYSTEM_NOTICE, null);
        NoticeStatus noticeStatus = notice.getNoticeStatus();
        if (DRAFT == noticeStatus) {
            // 草稿 -> 直接删除
            sysNoticeDao.removeById(noticeId);
            for (SysAttach attach : attachList) {
                sysAttachService.remove(attach);
            }
        } else if (PUBLISH == noticeStatus) {
            // 已发布 -> 改为撤回
            sysNoticeDao.updateStatus(noticeId, RECALL);
        } else if (RECALL == noticeStatus) {
            // 已撤回 -> 直接删除
            sysNoticeDao.removeById(noticeId);
            for (SysAttach attach : attachList) {
                sysAttachService.remove(attach);
            }
            sysNoticeUserDao.removeByNoticeId(noticeId);
        }
    }

    @Override
    public void edit(String tenantId, NoticeCreate sysNotice) throws Exception {
        HttpAsserts.notNull(sysNotice.getNoticeId(), BAD_REQUEST, "{admin.notice.id.null}");

        SysNotice notice = sysNoticeDao.getById(tenantId, sysNotice.getNoticeId());
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", sysNotice.getNoticeId());
        HttpAsserts.equals(DRAFT, notice.getNoticeStatus(), BAD_REQUEST, "{admin.notice.edit.unpublish}");

        if (!Access.isAdminUser()) {
            HttpAsserts.equals(notice.getCreateBy(), FORBIDDEN, Access.userCode(), "{admin.notice.edit.self}");
        }
        sysNoticeDao.updateNotice(sysNotice);
        filterAttaches(sysNotice);
    }

    private void filterAttaches(NoticeCreate sysNotice) throws Exception {
        String content = sysNotice.getContent();
        // 删掉content中不存在的附件
        List<AttachView> attaches = sysNotice.getAttaches();
        for (AttachView attach : attaches) {
            if (content.contains(attach.getAttachPath())) {
                sysAttachDao.updateOwnerById(String.valueOf(sysNotice.getNoticeId()), attach.getAttachId());
            } else {
                sysAttachService.removeById(attach.getAttachId());
            }
        }
    }

    @Override
    public void publish(String tenantId, Long noticeId) {
        SysNotice notice = sysNoticeDao.getById(tenantId, noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", noticeId);
        if (!Access.isAdminUser()) {
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.publish.self}");
        }
        if (DRAFT != notice.getNoticeStatus()) {
            return;
        }

        // 转换read信息
        if (YES == notice.getGoalsAll()) {
            sysNoticeDtoMapper.insertReadOfAll(tenantId, noticeId);
            sysNoticeDtoMapper.insertReadOfLdap(noticeId);
            sysNoticeDtoMapper.insertReadOfOauth(noticeId);
        } else {
            sysNoticeDtoMapper.insertReadOfDept(tenantId, noticeId, notice.getGoalsDept());
            sysNoticeDtoMapper.insertReadOfRole(tenantId, noticeId, notice.getGoalsRole());
            sysNoticeDtoMapper.insertReadOfUser(tenantId, noticeId, notice.getGoalsUser());
        }
        sysNoticeDtoMapper.updateMsgStat(noticeId, PUBLISH, new Date());
        // 推送
        List<String> userCodes = sysNoticeUserDao.getUserCodesByNoticeId(noticeId);
        socketIoHelper.sendClientsOfNamespace(SPACE_NOTICE, userCodes, EVENT_SERVER_NOTICE_NEW, notice.getNoticeTitle());
    }

    @Override
    public Response.Page<NoticeUserDto> getNoticeReaders(String tenantId, Long noticeId) {
        SysNotice notice = sysNoticeDao.getById(tenantId, noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", noticeId);

        Page<SysNoticeUser> page = sysNoticeUserDao.queryPageByNoticeId(noticeId);
        List<NoticeUserDto> dtoList = new ArrayList<>();

        List<String> sysCodes = new ArrayList<>();
        List<String> ldapCodes = new ArrayList<>();
        List<String> gitlabCodes = new ArrayList<>();
        for (SysNoticeUser noticeUser : page.getRecords()) {
            dtoList.add(BeanUtil.copyProperties(noticeUser, NoticeUserDto.class));
            String userCode = noticeUser.getUserCode();
            if (SYS.equalsType(userCode)) {
                sysCodes.add(userCode);
            } else if (LDAP.equalsType(userCode)) {
                ldapCodes.add(userCode);
            } else if (GITLAB.equalsType(userCode)) {
                gitlabCodes.add(userCode);
            }
        }
        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for (NoticeUserDto userDto : dtoList) {
            userDto.setUserName(sysMap.get(userDto.getUserCode()));
        }
        return new Response.Page<>(dtoList, page.getTotal());
    }

    @Override
    public Page<NoticeMsgDto> msgList() {
        Page<NoticeMsgDto> page = sysNoticeDtoMapper.msgList(Access.page(), Access.userCode());
        List<String> sysCodes = new ArrayList<>();
        List<String> ldapCodes = new ArrayList<>();
        List<String> gitlabCodes = new ArrayList<>();
        for (NoticeMsgDto msgDto : page.getRecords()) {
            String userCode = msgDto.getCreateBy();
            if (SYS.equalsType(userCode)) {
                sysCodes.add(userCode);
            } else if (LDAP.equalsType(userCode)) {
                ldapCodes.add(userCode);
            } else if (GITLAB.equalsType(userCode)) {
                gitlabCodes.add(userCode);
            }
        }
        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for (NoticeMsgDto msgDto : page.getRecords()) {
            msgDto.setCreateBy(sysMap.get(msgDto.getCreateBy()));
        }
        return page;
    }

    @Override
    public void msgRead(Long noticeId) {
        boolean statusUpdated = sysNoticeUserDao.updateReadStatus(Access.userCode(), noticeId, Access.accessTime());
        if (statusUpdated) {
            sysNoticeDtoMapper.updateReadStat(noticeId);
        }
    }

    @Override
    public void msgBack(NoticeMsgBack msgBack) {
        sysNoticeUserDao.updateReadBack(Access.userCode(), msgBack.getNoticeId(), msgBack.getReadBack());
    }

    @Override
    public void msgDelete(Long msgId) {
        sysNoticeDtoMapper.msgDelete(Access.userCode(), msgId);
    }

    @Override
    public long msgUnReadCount(String userCode) {
        return sysNoticeUserDao.countUnReadByUserId(userCode);
    }

    @Override
    public void sendUserNotice(SysNotice notice, Integer userId) {
        sysNoticeDao.save(notice);
//        sysNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(userId));
    }

    public void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser) {
        String startUserName = sysUserDao.queryNameById(startUser);
        SysNotice notice = new SysNotice();
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
        sysNoticeDao.save(notice);
//        sysNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(assigneeUser));
//        socketIoHelper.sendSingle("flow_notice", "催办提醒: " + startUserName + "的" + processName + "[" + taskName + "]", assigneeUser);
    }
}
