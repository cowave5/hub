/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.sys.code.api.entity.DbTableIndex;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface IndexMapper {

    /**
     * 新增
     */
    void insert(DbTableIndex index);

    /**
     * 列表
     */
    List<DbTableIndex> list(Long tableId);

    /**
     * 删除库索引信息
     */
    void deleteDbIndex(Long tableId);
}
