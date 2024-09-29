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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.base.enums.NoticeStatus.*;
import static com.cowave.sys.admin.domain.rabc.enums.AccessType.*;
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
    public Response.Page<NoticeDto> list(NoticeQuery query) {
        List<String> sysCodes = new ArrayList<>();
        List<String> ldapCodes = new ArrayList<>();
        List<String> gitlabCodes = new ArrayList<>();

        Page<SysNotice> page = sysNoticeDao.queryPageOfUser(query);
        List<NoticeDto> dtoList = new ArrayList<>();
        for(SysNotice sysNotice : page.getRecords()){
            dtoList.add(BeanUtil.copyProperties(sysNotice, NoticeDto.class));
            String userCode = sysNotice.getCreateBy();
            if(SYS.isTypeEquals(userCode)){
                sysCodes.add(userCode);
            }else if(LDAP.isTypeEquals(userCode)){
                ldapCodes.add(userCode);
            }else if(GITLAB.isTypeEquals(userCode)){
                gitlabCodes.add(userCode);
            }
        }

        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for(NoticeDto dto : dtoList){
            dto.setCreateUserName(sysMap.get(dto.getCreateBy()));
        }
        return new Response.Page<>(dtoList, page.getTotal());
    }

    @Override
    public NoticeDto info(Long noticeId) {
        SysNotice sysNotice = sysNoticeDao.getById(noticeId);
        NoticeDto infoDto = BeanUtil.copyProperties(sysNotice, NoticeDto.class);
        String userCode = infoDto.getCreateBy();
        if (SYS.isTypeEquals(userCode)) {
            infoDto.setCreateUserName(sysUserDao.queryNameByCode(userCode));
        } else if (LDAP.isTypeEquals(userCode)) {
            infoDto.setCreateUserName(ldapUserDao.queryNameByCode(userCode));
        } else if (GITLAB.isTypeEquals(userCode)) {
            infoDto.setCreateUserName(oauthUserDao.queryNameByCode(userCode));
        }
        return infoDto;
    }

    @Override
    public void add(NoticeCreate sysNotice) throws Exception {
        sysNoticeDao.save(sysNotice);
        // 图片附件
        filterAttaches(sysNotice);
    }

    @Override
    public void delete(List<Long> noticeIds) throws Exception {
        for(Long noticeId : noticeIds){
            delete(noticeId);
        }
    }

    private void delete(Long noticeId) throws Exception {
        SysNotice notice = sysNoticeDao.getById(noticeId);
        if(notice == null){
            return;
        }

        if(!Access.isAdminUser()){
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.delete.self}");
        }

        List<SysAttach> attachList = sysAttachDao.queryList(noticeId, "sys-notice", null);

        // 删除草稿
        if(DRAFT.isEqual(notice.getNoticeStatus())){
            sysNoticeDao.removeById(noticeId);
            for(SysAttach attach : attachList){
                sysAttachService.delete(attach);
            }
        }else if(PUBLISH.isEqual(notice.getNoticeStatus())){
            // 撤回已发布
            sysNoticeDao.updateStatus(noticeId, RECALL.val());
        }else if(RECALL.isEqual(notice.getNoticeStatus())) {
            // 删除已撤回
            sysNoticeDao.removeById(noticeId);
            for(SysAttach attach : attachList){
                sysAttachService.delete(attach);
            }
            sysNoticeUserDao.removeByNoticeId(noticeId);
        }
    }

    @Override
    public void edit(NoticeCreate sysNotice) throws Exception {
        HttpAsserts.notNull(sysNotice.getNoticeId(), BAD_REQUEST, "{admin.notice.id.notnull}");

        SysNotice notice = sysNoticeDao.getById(sysNotice.getNoticeId());
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.notexist}", sysNotice.getNoticeId());
        HttpAsserts.isTrue(DRAFT.isEqual(notice.getNoticeStatus()), BAD_REQUEST, "{admin.notice.edit.unpublish}");

        if(!Access.isAdminUser()){
            HttpAsserts.equals(notice.getCreateBy(), FORBIDDEN, Access.userCode(), "{admin.notice.edit.self}");
        }

        sysNoticeDao.updateNotice(sysNotice);
        filterAttaches(sysNotice);
    }

    private void filterAttaches(NoticeCreate sysNotice) throws Exception {
        String content = sysNotice.getContent();
        // 删掉content中不存在的附件
        List<AttachView> attaches = sysNotice.getAttaches();
        for(AttachView attach : attaches){
            if(content.contains(attach.getAttachPath())){
                sysAttachService.updateMasterById(sysNotice.getNoticeId(), attach.getAttachId());
            }else{
                sysAttachService.delete(attach.getAttachId());
            }
        }
    }

    @Override
    public void publish(Long noticeId) {
        SysNotice notice = sysNoticeDao.getById(noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.notexist}", noticeId);
        if(!Access.isAdminUser()){
            HttpAsserts.equals(notice.getCreateBy(), Access.userCode(), FORBIDDEN, "{admin.notice.publish.self}");
        }

        if(!DRAFT.isEqual(notice.getNoticeStatus())){
            return;
        }

        // 转换read信息
        if(Objects.equals(notice.getGoalsAll(), 1)) {
            sysNoticeDtoMapper.insertReadOfAll(noticeId);
            sysNoticeDtoMapper.insertReadOfLdap(noticeId);
            sysNoticeDtoMapper.insertReadOfOauth(noticeId);
        } else {
            sysNoticeDtoMapper.insertReadOfDept(noticeId, notice.getGoalsDept());
            sysNoticeDtoMapper.insertReadOfRole(noticeId, notice.getGoalsRole());
            sysNoticeDtoMapper.insertReadOfUser(noticeId, notice.getGoalsUser());
        }
        sysNoticeDtoMapper.updateMsgStat(noticeId, PUBLISH.val(), new Date());

        // 推送
        List<String> userCodes = sysNoticeUserDao.getUserCodesByNoticeId(noticeId);
        socketIoHelper.sendClientsOfNamespace(SPACE_NOTICE, userCodes, EVENT_SERVER_NOTICE_NEW, notice.getNoticeTitle());
    }

    @Override
    public SysAttach imageUpload(MultipartFile file) throws Exception {
        AttachUpload upload = new AttachUpload(1, null, "sys-notice", "images");
        SysAttach attach = sysAttachService.upload(file, upload);
        attach.setViewUrl(sysAttachService.preview(attach));
        return attach;
    }

    @Override
    public Response.Page<NoticeUserDto> getNoticeReaders(Long noticeId) {
        List<String> sysCodes = new ArrayList<>();
        List<String> ldapCodes = new ArrayList<>();
        List<String> gitlabCodes = new ArrayList<>();

        Page<SysNoticeUser> page = sysNoticeUserDao.queryPageByNoticeId(noticeId);
        List<NoticeUserDto> dtoList = new ArrayList<>();
        for(SysNoticeUser noticeUser : page.getRecords()){
            dtoList.add(BeanUtil.copyProperties(noticeUser, NoticeUserDto.class));

            String userCode = noticeUser.getUserCode();
            if(SYS.isTypeEquals(userCode)){
                sysCodes.add(userCode);
            }else if(LDAP.isTypeEquals(userCode)){
                ldapCodes.add(userCode);
            }else if(GITLAB.isTypeEquals(userCode)){
                gitlabCodes.add(userCode);
            }
        }

        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for(NoticeUserDto userDto : dtoList){
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

        for(NoticeMsgDto msgDto : page.getRecords()){
            String userCode = msgDto.getCreateBy();
            if(SYS.isTypeEquals(userCode)){
                sysCodes.add(userCode);
            }else if(LDAP.isTypeEquals(userCode)){
                ldapCodes.add(userCode);
            }else if(GITLAB.isTypeEquals(userCode)){
                gitlabCodes.add(userCode);
            }
        }

        Map<String, String> sysMap = sysUserDao.queryCodeNameMap(sysCodes);
        sysMap.putAll(ldapUserDao.queryCodeNameMap(ldapCodes));
        sysMap.putAll(oauthUserDao.queryCodeNameMap(gitlabCodes));
        for(NoticeMsgDto msgDto : page.getRecords()){
            msgDto.setCreateBy(sysMap.get(msgDto.getCreateBy()));
        }
        return page;
    }

    @Override
    public void msgRead(Long noticeId) {
        boolean statusUpdated = sysNoticeUserDao.updateReadStatus(Access.userCode(), noticeId, Access.accessTime());
        if(statusUpdated){
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
        sysNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(userId));
    }

    public void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser){
        String startUserName = sysUserDao.queryNameById(startUser);
        SysNotice notice = new SysNotice();
        notice.setNoticeStatus(PUBLISH.val());
        notice.setCreateBy(Access.userCode());
        notice.setNoticeType(3);
        notice.setNoticeLevel(0);
        notice.setIsSystem(0);
        notice.setStatTotal(1);
        notice.setStatRead(0);
        notice.setCreateTime(new Date());
        notice.setPublishTime(new Date());
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        sysNoticeDao.save(notice);
        sysNoticeDtoMapper.insertReadOfUser(notice.getNoticeId(), List.of(assigneeUser));
//        socketIoHelper.sendSingle("flow_notice", "催办提醒: " + startUserName + "的" + processName + "[" + taskName + "]", assigneeUser);
    }
}
