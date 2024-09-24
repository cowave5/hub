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
import com.cowave.sys.code.api.entity.DbInfo;

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
