/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service;

import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.entity.DbTable;

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
