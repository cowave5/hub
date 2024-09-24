/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.sys.code.api.entity.AppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface AppMapper {

    /**
     * 列表
     */
    List<AppInfo> list(AppInfo appInfo);

    /**
     * 详情
     */
    AppInfo info(Long id);

    /**
     * 模型列表
     */
    List<Long> modelList(Long id);

    /**
     * 新增
     */
    void insert(AppInfo appInfo);

    /**
     * 新增应用模型
     */
    void insertModels(@Param("appId") Long appId, @Param("list") List<Long> list);

    /**
     * 删除应用模型
     */
    void deleteModels(Long id);

    /**
     * 编辑
     */
    void update(AppInfo appInfo);

    /**
     * 删除
     */
    void delete(Integer[] array);
}
