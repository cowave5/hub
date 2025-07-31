/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.infra.code.dao.mapper.dto;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.sys.meter.domain.code.ModelField;
import com.cowave.sys.meter.domain.code.TypeField;
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
    void switchNotnull(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否集合字段切换
     */
    void switchCollect(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否Excel字段切换
     */
    void switchExcel(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否where条件切换
     */
    void switchWhere(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否列表字段切换
     */
    void switchList(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否详情字段切换
     */
    void switchInfo(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否新增字段切换
     */
    void switchInsert(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否修改字段切换
     */
    void switchEdit(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 清除Model字段数据
     */
    void clearModelFields(Integer[] array);
}
