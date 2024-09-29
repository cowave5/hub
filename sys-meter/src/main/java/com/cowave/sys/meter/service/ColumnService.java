/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service;

import com.cowave.sys.meter.entity.SelectOption;
import com.cowave.sys.meter.entity.DbTableColumn;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface ColumnService {

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
    void add(DbTableColumn tableColumn);

    /**
     * 编辑
     */
    void edit(DbTableColumn tableColumn);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 切换非空
     */
    void switchNotnull(Long id, Integer flag);

    /**
     * 切换主键
     */
    void switchPrimary(Long id, Integer flag);

    /**
     * 自增主键
     */
    void switchIncrement(Long id, Integer flag);
}
