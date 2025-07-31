/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service.code;

import com.cowave.sys.meter.domain.code.DbInfo;
import com.cowave.sys.meter.domain.code.SelectOption;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 */
public interface DbService {

    /**
     * 数据库选项
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
    void add(DbInfo dbInfo);

    /**
     * 编辑
     */
    void edit(DbInfo dbInfo);

    /**
     * 删除
     */
    void delete(Long[] id);

    /**
     * 编辑
     */
    void synTable(DbInfo dbInfo) throws Exception;

    /**
     * DDL预览
     */
    Map<String, String> preview(Long id);

    /**
     * DDL模板
     */
    byte[] template(Long id) throws IOException;
}
