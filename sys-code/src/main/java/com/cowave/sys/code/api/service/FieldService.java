/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service;

import com.cowave.sys.code.api.entity.ModelField;
import com.cowave.sys.code.api.entity.TypeField;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 */
public interface FieldService {

    /**
     * Excel转换器
     */
    List<Map<String, String>> excelConverter();

    /**
     * 字段类型
     */
    List<TypeField> types();

    /**
     * 列表
     */
    List<ModelField> list(Long modelId);

    /**
     * 详情
     */
    ModelField info(Long id);

    /**
     * 新增
     */
    void add(ModelField modelField);

    /**
     * 编辑
     */
    void edit(ModelField modelField);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 切换非空
     */
    void switchNotnull(Long id, Integer flag);

    /**
     * 是否集合字段切换
     */
    void switchCollect(Long id, Integer flag);

    /**
     * 是否Excel字段切换
     */
    void switchExcel(Long id, Integer flag);

    /**
     * 是否where条件切换
     */
    void switchWhere(Long id, Integer flag);

    /**
     * 是否列表字段切换
     */
    void switchList(Long id, Integer flag);

    /**
     * 是否详情字段切换
     */
    void switchInfo(Long id, Integer flag);

    /**
     * 是否新增字段切换
     */
    void switchInsert(Long id, Integer flag);

    /**
     * 是否修改字段切换
     */
    void switchEdit(Long id, Integer flag);
}
