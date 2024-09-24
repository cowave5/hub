/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.entity;

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
