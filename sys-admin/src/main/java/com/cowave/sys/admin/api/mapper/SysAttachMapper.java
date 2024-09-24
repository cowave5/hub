/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysAttach;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysAttachMapper {

    /**
     * 新增
     */
    void insert(SysAttach sysAttach);

    /**
     * 删除
     */
    void delete(Long attachId);

    /**
     * 更新宿主id
     */
    void updateMasterById(@Param("masterId") Long masterId, @Param("attachId") Long attachId);

    /**
     * 宿主最新附件
     */
    SysAttach latestOfMaster(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup);

    /**
     * 列表
     */
    List<SysAttach> list(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);

    /**
     * 详情
     */
    SysAttach info(Long attachId);

    /**
     * 更新宿主
     */
    void masterUpdate(@Param("list") List<Long> attachIds, @Param("masterId") Long masterId);

    /**
     * 清除附件
     */
    void masterClear(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);

    /**
     * 删除宿主
     */
    void masterDelete(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);
}
