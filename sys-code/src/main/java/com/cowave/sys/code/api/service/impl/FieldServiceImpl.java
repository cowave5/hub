/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service.impl;

import com.alibaba.excel.converters.Converter;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.code.api.entity.ModelField;
import com.cowave.sys.code.api.mapper.FieldMapper;
import com.cowave.sys.code.api.service.FieldService;
import com.cowave.sys.code.api.entity.TypeField;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
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
    public List<Map<String, String>> excelConverter() {
        List<Map<String, String>> list = new ArrayList<>();
        Reflections reflections = new Reflections("com.cowave");
        Set<Class<? extends Converter>> classes = reflections.getSubTypesOf(Converter.class);
        for (Class<?> clazz : classes) {
            Map<String, String> map = new HashMap<>();
            map.put("key", clazz.getName());
            map.put("label", clazz.getName());
            list.add(map);
        }
        return list;
    }

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
        fieldMapper.switchNotnull(id, flag, Access.accessUser());
    }

    @Override
    public void switchCollect(Long id, Integer flag) {
        fieldMapper.switchCollect(id, flag, Access.accessUser());
    }

    @Override
    public void switchExcel(Long id, Integer flag) {
        fieldMapper.switchExcel(id, flag, Access.accessUser());
    }

    @Override
    public void switchWhere(Long id, Integer flag) {
        fieldMapper.switchWhere(id, flag, Access.accessUser());
    }

    @Override
    public void switchList(Long id, Integer flag) {
        fieldMapper.switchList(id, flag, Access.accessUser());
    }

    @Override
    public void switchInfo(Long id, Integer flag) {
        fieldMapper.switchInfo(id, flag, Access.accessUser());
    }

    @Override
    public void switchInsert(Long id, Integer flag) {
        fieldMapper.switchInsert(id, flag, Access.accessUser());
    }

    @Override
    public void switchEdit(Long id, Integer flag) {
        fieldMapper.switchEdit(id, flag, Access.accessUser());
    }
}
