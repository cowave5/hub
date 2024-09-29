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

import com.cowave.commons.client.http.asserts.AssertsException;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.meter.entity.DbTableColumn;
import com.cowave.sys.meter.entity.SelectOption;
import com.cowave.sys.meter.mapper.ColumnMapper;
import com.cowave.sys.meter.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnMapper columnMapper;

    @Override
    public List<SelectOption> options(Long modelId) {
        return columnMapper.options(modelId);
    }

    @Override
    public List<DbTableColumn> list(Long tableId) {
        return columnMapper.list(tableId);
    }

    @Override
    public DbTableColumn info(Long id) {
        return columnMapper.info(id);
    }

    @Override
    public void add(DbTableColumn tableColumn) {
        try{
            columnMapper.insert(tableColumn);
        }catch(DuplicateKeyException e){
            throw new AssertsException("字段[" + tableColumn.getColumnName() + "]存在冲突");
        }
    }

    @Override
    public void edit(DbTableColumn tableColumn) {
        columnMapper.update(tableColumn);
    }

    @Override
    public void delete(Integer[] id) {
        columnMapper.delete(id);
    }

    @Override
    public void switchNotnull(Long id, Integer flag) {
        columnMapper.switchNotnull(id, flag, Access.accessInfo());
    }

    @Override
    public void switchPrimary(Long id, Integer flag) {
        columnMapper.switchPrimary(id, flag, Access.accessInfo());
    }

    @Override
    public void switchIncrement(Long id, Integer flag) {
        columnMapper.switchIncrement(id, flag, Access.accessInfo());
    }
}
