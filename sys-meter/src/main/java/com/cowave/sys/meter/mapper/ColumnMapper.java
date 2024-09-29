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

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.sys.meter.entity.DbTableColumn;
import com.cowave.sys.meter.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface ColumnMapper {

    /**
     * 字段选项
     */
    List<SelectOption> options(Long modelId);

    /**
     * 列表
     */
    List<DbTableColumn> list(Long tableId);

    /**
     * 详情
     */
    DbTableColumn info(Long id);

    /**
     * 新增
     */
    void insert(DbTableColumn tableColumn);

    /**
     * 编辑
     */
    void update(DbTableColumn tableColumn);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * 切换非空
     */
    void switchNotnull(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 切换主键
     */
    void switchPrimary(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 自增主键
     */
    void switchIncrement(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 清除DB字段数据
     */
    void deleteDbColumns(Long dbId);
}
