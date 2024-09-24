/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service.impl;

import com.cowave.sys.code.api.entity.*;
import com.cowave.sys.code.api.mapper.ColumnMapper;
import com.cowave.sys.code.api.mapper.IndexMapper;
import com.cowave.sys.code.api.mapper.TableMapper;
import com.cowave.sys.code.api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.*;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class TableServiceImpl implements TableService {

    private final TableMapper tableMapper;

    private final ColumnMapper columnMapper;

    private final IndexMapper indexMapper;

    @Override
    public List<SelectOption> options(Long appId) {
        return tableMapper.options(appId);
    }

    @Override
    public List<DbTable> list(DbTable dbTable) {
        return tableMapper.list(dbTable);
    }

    @Override
    public DbTable info(Long id) {
        return tableMapper.info(id);
    }

    @Override
    public void add(DbTable dbTable) {
        tableMapper.insert(dbTable);
    }

    @Override
    public void edit(DbTable dbTable) {
        tableMapper.update(dbTable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer[] id) {
        tableMapper.delete(id);
        tableMapper.deleteTableColumns(id);
    }

    @Override
    public Map<String, String> preview(Long id) {
        VelocityContext velocityContext = prepareVelocity(id);
        Map<String, String> sqlMap = new LinkedHashMap<>();
        List<String> templates = templateList();
        for (String template : templates) {
            StringWriter stringWriter = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(velocityContext, stringWriter);
            sqlMap.put(template, stringWriter.toString());
            IOUtils.closeQuietly(stringWriter);
        }
        return sqlMap;
    }

    private VelocityContext prepareVelocity(Long id) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init(properties);

        DbTable table = tableMapper.info(id);
        List<DbTableColumn> columns = columnMapper.list(id);
        List<DbTableIndex> indexs = indexMapper.list(id);
        List<DbTableIndex> merges = new ArrayList<>();
        DbTableIndex index = null;
        for(DbTableIndex i : indexs){
            if(index == null || !index.getIndexName().equals(i.getIndexName())){
                index = i;
                merges.add(index);
            }
            index.getColumns().add(i.getColumnName());
        }
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", table.getTableName());
        velocityContext.put("tableComment", table.getTableComment());
        velocityContext.put("columns", columns);
        velocityContext.put("indexs", merges);
        return velocityContext;
    }

    private List<String> templateList() {
        List<String> templates = new ArrayList<>();
        templates.add("vm/table/oscar.sql.vm");
        templates.add("vm/table/mysql.sql.vm");
        templates.add("vm/table/postgres.sql.vm");
        return templates;
    }
}
