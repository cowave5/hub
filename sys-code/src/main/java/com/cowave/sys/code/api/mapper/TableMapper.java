/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.sys.code.api.entity.DbTable;
import com.cowave.sys.code.api.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface TableMapper {

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
    void insert(DbTable dbTable);

    /**
     * 更新
     */
    void update(DbTable dbTable);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * 删除表字段
     */
    void deleteTableColumns(Integer[] array);

    /**
     * 清除DB表数据
     */
    void deleteDbTables(Long dbId);

    /**
     * 统计数据库下已绑定模型的表
     */
    int countTableModels(Long dbId);
}
