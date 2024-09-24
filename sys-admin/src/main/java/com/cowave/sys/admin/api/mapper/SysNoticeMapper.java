/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysNoticeMapper {

    /**
     * 列表
     */
    Page<SysNotice> list(Page<SysNotice> page, @Param("notice") SysNotice sysNotice);

    /**
     * 用户创建的
     */
    Page<SysNotice> listOfUser(Page<SysNotice> page, @Param("userId") Long userId, @Param("notice") SysNotice sysNotice);

    /**
     * 详情
     */
    SysNotice info(Long noticeId);

    /**
     * 新增
     */
    void insert(SysNotice sysNotice);

    /**
     * 创建Notice
     */
    void createNotice(SysNotice sysNotice);

    /**
     * 更新
     */
    void update(SysNotice sysNotice);

    /**
     * 删除
     */
    void delete(Long noticeId);

    /**
     * 修改状态
     */
    void updateStatus(@Param("noticeId") Long noticeId, @Param("noticeStatus") Integer noticeStatus);

    /**
     * 消息撤回
     */
    void msgRecall(@Param("noticeId") Long noticeId);

    /**
     * 消息删除
     */
    void msgClear(@Param("noticeId") Long noticeId);

    /**
     * 所有用户
     */
    void insertReadOfAll(Long noticeId);

    /**
     * 单位用户
     */
    void insertReadOfDept(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 角色用户
     */
    void insertReadOfRole(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 指定用户
     */
    void insertReadOfUser(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 更新待读总数
     */
    void updatePublishTotal(@Param("noticeId") Long noticeId,
                            @Param("noticeStatus") Integer noticeStatus, @Param("publishTime") Date publishTime);

    /**
     * 已读列表
     */
    Page<SysNoticeRead> readList(Page<SysNoticeRead> page, @Param("noticeId") Long noticeId);

    /**
     * 消息列表
     */
    Page<SysNotice> msgList(Page<SysNotice> page, @Param("userId") Long userId);

    /**
     * 阅读消息
     */
    int msgRead(@Param("noticeId") Long noticeId, @Param("noticeStatus") Integer noticeStatus,
                @Param("userId") Long userId, @Param("readTime") Date readTime);

    /**
     * 反馈消息
     */
    void msgBack(@Param("noticeId") Long noticeId, @Param("userId") Long userId, @Param("readBack") String readBack);

    /**
     * 更新已读统计
     */
    void updateReadStat(Long noticeId);

    /**
     * 删除消息
     */
    void msgDelete(@Param("userId") Long userId, @Param("noticeId") Long noticeId);

    /**
     * 未读消息
     */
    int countUserUnRead(Long userId);

    /**
     * 消息目标用户
     */
    List<Long> publishUserIds(Long noticeId);
}
