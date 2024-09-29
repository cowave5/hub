/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.mapper;

import com.cowave.sys.meter.entity.DbInfo;
import com.cowave.sys.meter.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface DbMapper {

    /**
     * 项目选项
     */
    List<SelectOption> options(Long projectId);

    /**
     * 列表
     */
    List<DbInfo> list(DbInfo dbInfo);

    /**
     * 详情
     */
    DbInfo info(Long id);

    /**
     * 新增
     */
    void insert(DbInfo dbInfo);

    /**
     * 更新
     */
    void update(DbInfo dbInfo);

    /**
     * 删除
     */
    void delete(Long[] array);

    /**
     * 更新连接信息
     */
    void updateSynInfo(DbInfo dbInfo);
}
