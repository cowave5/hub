/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.infra.code.dao.mapper.dto;

import com.cowave.sys.meter.domain.code.AppInfo;
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
