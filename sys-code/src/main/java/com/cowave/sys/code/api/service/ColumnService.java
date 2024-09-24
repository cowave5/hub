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
import com.cowave.sys.code.api.entity.DbTableColumn;

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
