/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.domain.code;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 索引信息
 *
 * @author shanhuiming
 */
@Data
public class DbTableIndex {

    /**
     * id
     */
    private Long id;

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 索引类型
     */
    private String indexType;

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 是否唯一索引
     */
    private int isUnique;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段位置
     */
    private int columnPosition;

    /**
     * 是否主键
     */
    private int isPrimary;

    /**
     * 字段名
     */
    private List<String> columns = new ArrayList<>();
}
