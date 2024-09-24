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

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.cowave.sys.model.admin.SysAttach;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysAttachService {

    /**
     * 上传
     */
    SysAttach upload(MultipartFile file, SysAttach sysAttach, boolean isPublic) throws Exception;

    /**
     * 预览
     */
    String preview(Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(SysAttach sysAttach) throws Exception;

    /**
     * 删除
     */
    void delete(Long attachId) throws Exception;

    /**
     * 删除
     */
    void delete(SysAttach sysAttach) throws Exception;

    /**
     * 保留最近几个
     */
    void masterReserve(Long masterId, String attachGroup, String attachType, int reserve) throws Exception;

    /**
     * 更新宿主id
     */
    void updateMasterById(Long masterId, Long attachId);

    /**
     * 宿主最新附件
     */
    SysAttach latestOfMaster(Long masterId, String attachGroup) throws Exception;

    /**
     * 下载
     */
    void download(HttpServletResponse response, Long attachId) throws Exception;

    /**
     * 列表
     */
    List<SysAttach> list(Long masterId, String attachGroup, String attachType);

    /**
     * 更新宿主
     */
    void masterUpdate(List<Long> attachIds, Long masterId);

    /**
     * 删除宿主
     */
    void masterDelete(Long masterId, String attachGroup, String attachType) throws Exception;
}
