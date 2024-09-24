/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.sys.code.api.entity.DbSequence;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface SequenceMapper {

    /**
     * 新增
     */
    void insert(DbSequence sequence);

    /**
     * 库序列信息
     */
    List<DbSequence> list(Long dbId);

    /**
     * 删除库序列信息
     */
    void deleteDbSequence(Long dbId);
}
