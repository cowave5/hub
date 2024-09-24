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
import com.cowave.sys.code.api.entity.ModelField;
import com.cowave.sys.code.api.entity.TypeField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface FieldMapper {

    /**
     * 字段类型
     */
    List<TypeField> types(Long appId);

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
    void insert(ModelField modelField);

    /**
     * 编辑
     */
    void update(ModelField modelField);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * 是否非空字段切换
     */
    void switchNotnull(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否集合字段切换
     */
    void switchCollect(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否Excel字段切换
     */
    void switchExcel(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否where条件切换
     */
    void switchWhere(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否列表字段切换
     */
    void switchList(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否详情字段切换
     */
    void switchInfo(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否新增字段切换
     */
    void switchInsert(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否修改字段切换
     */
    void switchEdit(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 清除Model字段数据
     */
    void clearModelFields(Integer[] array);
}
