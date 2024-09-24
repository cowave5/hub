/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.AssertsException;
import com.cowave.sys.code.api.entity.SelectOption;
import com.cowave.sys.code.api.entity.DbTableColumn;
import com.cowave.sys.code.api.mapper.ColumnMapper;
import com.cowave.sys.code.api.service.ColumnService;
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
        columnMapper.switchNotnull(id, flag, Access.accessUser());
    }

    @Override
    public void switchPrimary(Long id, Integer flag) {
        columnMapper.switchPrimary(id, flag, Access.accessUser());
    }

    @Override
    public void switchIncrement(Long id, Integer flag) {
        columnMapper.switchIncrement(id, flag, Access.accessUser());
    }
}
