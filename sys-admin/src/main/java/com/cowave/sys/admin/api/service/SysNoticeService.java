/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;
import com.cowave.sys.model.admin.SysUser;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysNoticeService {

    /**
     * 列表
     */
    Page<SysNotice> list(SysNotice sysNotice);

    /**
     * 详情
     */
    SysNotice info(Long noticeId);

    /**
     * 新增
     */
    void add(SysNotice sysNotice) throws Exception;

    /**
     * 修改
     */
    void edit(SysNotice sysNotice) throws Exception;

    /**
     * 上传图片
     */
    SysAttach imageUpload(MultipartFile file, SysAttach image) throws Exception;

    /**
     * 删除
     */
    void delete(Long[] noticeId) throws Exception;

    /**
     * 发布
     */
    void publish(Long noticeId);

    /**
     * 已读列表
     */
    Page<SysNoticeRead> readList(Long noticeId);

    /**
     * 消息列表
     */
    Page<SysNotice> msgList();

    /**
     * 阅读消息
     */
    void msgRead(Long noticeId);

    /**
     * 反馈消息
     */
    void msgBack(Long noticeId, String readBack);

    /**
     * 删除消息
     */
    void msgDelete(Long noticeId);

    /**
     * 发送通知
     */
    void sendNotice(SysNotice notice, Long readUserId);

    /**
     * 流程通知
     */
    void sendFlowNotice(String processName, String taskName, Long startUser, Long assigneeUser);
}
