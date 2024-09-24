/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.sys.code.api.entity.DbInfo;
import com.cowave.sys.code.api.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface DbMapper {

    /**
     * 项目选项
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
    void insert(DbInfo dbInfo);

    /**
     * 更新
     */
    void update(DbInfo dbInfo);

    /**
     * 删除
     */
    void delete(Long[] array);

    /**
     * 更新连接信息
     */
    void updateSynInfo(DbInfo dbInfo);
}
