/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service.code;

import com.cowave.sys.meter.domain.code.ModelField;
import com.cowave.sys.meter.domain.code.TypeField;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface FieldService {

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
