/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.base.dto.NoticeMsgDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysNoticeDtoMapper {

    void insertReadOfAll(@Param("tenantId") String tenantId, @Param("noticeId") Long noticeId);

    void insertReadOfLdap(Long noticeId);

    void insertReadOfOauth(Long noticeId);

    /**
     * 单位用户
     */
    void insertReadOfDept(@Param("tenantId") String tenantId,
                          @Param("noticeId") Long noticeId,
                          @Param("list") List<Integer> list);

    /**
     * 角色用户
     */
    void insertReadOfRole(@Param("tenantId") String tenantId,
                          @Param("noticeId") Long noticeId,
                          @Param("list") List<Integer> list);

    /**
     * 指定用户
     */
    void insertReadOfUser(@Param("tenantId") String tenantId,
                          @Param("noticeId") Long noticeId,
                          @Param("list") List<Integer> list);

    /**
     * 更新待读总数
     */
    void updateMsgStat(@Param("noticeId") Long noticeId,
                       @Param("noticeStatus") Integer noticeStatus,
                       @Param("publishTime") Date publishTime);

    /**
     * 消息列表
     */
    Page<NoticeMsgDto> msgList(Page<NoticeMsgDto> page, @Param("userCode") String userCode);

    /**
     * 更新已读统计
     */
    void updateReadStat(Long noticeId);

    /**
     * 删除消息
     */
    void msgDelete(@Param("userCode") String userCode, @Param("msgId") Long msgId);

    /**
     * 新用户的消息
     */
    void initNoticeMsgForNewUser(String userCode);

    /**
     * 新用户更新消息统计
     */
    void updateNoticeStatForNewUser();
}
