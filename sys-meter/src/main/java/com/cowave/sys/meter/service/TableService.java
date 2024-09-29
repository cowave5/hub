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
import com.cowave.sys.meter.entity.DbTable;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 */
public interface TableService {

    /**
     * 表选项
     */
    List<SelectOption> options(Long appId);

    /**
     * 列表
     */
    List<DbTable> list(DbTable dbTable);

    /**
     * 详情
     */
    DbTable info(Long id);

    /**
     * 新增
     */
    void add(DbTable dbTable);

    /**
     * 编辑
     */
    void edit(DbTable dbTable);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * DDL预览
     */
    Map<String, String> preview(Long id);
}
