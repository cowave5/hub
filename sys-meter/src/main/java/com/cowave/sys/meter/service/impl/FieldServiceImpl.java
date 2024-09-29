/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.sys.meter.entity.ModelField;
import com.cowave.sys.meter.mapper.FieldMapper;
import com.cowave.sys.meter.service.FieldService;
import com.cowave.sys.meter.entity.TypeField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class FieldServiceImpl implements FieldService {

    private final FieldMapper fieldMapper;

    @Override
    public List<TypeField> types() {
        return new ArrayList<>(TypeField.TYPE_INTERNAL);
    }

    @Override
    public List<ModelField> list(Long modelId) {
        return fieldMapper.list(modelId);
    }

    @Override
    public ModelField info(Long id) {
        return fieldMapper.info(id);
    }

    @Override
    public void add(ModelField modelField) {
        fieldMapper.insert(modelField);
    }

    @Override
    public void edit(ModelField modelField) {
        fieldMapper.update(modelField);
    }

    @Override
    public void delete(Integer[] id) {
        fieldMapper.delete(id);
    }

    @Override
    public void switchNotnull(Long id, Integer flag) {
        fieldMapper.switchNotnull(id, flag, Access.accessInfo());
    }

    @Override
    public void switchCollect(Long id, Integer flag) {
        fieldMapper.switchCollect(id, flag, Access.accessInfo());
    }

    @Override
    public void switchExcel(Long id, Integer flag) {
        fieldMapper.switchExcel(id, flag, Access.accessInfo());
    }

    @Override
    public void switchWhere(Long id, Integer flag) {
        fieldMapper.switchWhere(id, flag, Access.accessInfo());
    }

    @Override
    public void switchList(Long id, Integer flag) {
        fieldMapper.switchList(id, flag, Access.accessInfo());
    }

    @Override
    public void switchInfo(Long id, Integer flag) {
        fieldMapper.switchInfo(id, flag, Access.accessInfo());
    }

    @Override
    public void switchInsert(Long id, Integer flag) {
        fieldMapper.switchInsert(id, flag, Access.accessInfo());
    }

    @Override
    public void switchEdit(Long id, Integer flag) {
        fieldMapper.switchEdit(id, flag, Access.accessInfo());
    }
}
