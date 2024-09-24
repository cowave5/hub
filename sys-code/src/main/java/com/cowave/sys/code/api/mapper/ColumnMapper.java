/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.commons.framework.access.AccessUser;
import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.entity.DbTableColumn;
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
    void switchNotnull(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 切换主键
     */
    void switchPrimary(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 自增主键
     */
    void switchIncrement(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 清除DB字段数据
     */
    void deleteDbColumns(Long dbId);
}
