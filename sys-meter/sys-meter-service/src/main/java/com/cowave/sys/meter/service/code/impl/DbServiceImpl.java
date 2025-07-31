/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service.code.impl;

import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.client.http.asserts.AssertsException;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.meter.domain.code.*;
import com.cowave.sys.meter.infra.code.dao.mapper.dto.*;
import com.cowave.sys.meter.service.code.DbService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class DbServiceImpl implements DbService {

    private final DbMapper dbMapper;

    private final TableMapper tableMapper;

    private final ColumnMapper columnMapper;

    private final IndexMapper indexMapper;

    private final SequenceMapper sequenceMapper;

    @Override
    public List<SelectOption> options(Long projectId) {
        return dbMapper.options(projectId);
    }

    @Override
    public List<DbInfo> list(DbInfo dbInfo) {
        return dbMapper.list(dbInfo);
    }

    @Override
    public DbInfo info(Long id) {
        return dbMapper.info(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(DbInfo dbInfo) {
        dbMapper.insert(dbInfo);
    }

    @Override
    public void edit(DbInfo dbInfo) {
        dbMapper.update(dbInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] ids) {
        dbMapper.delete(ids);
        for(Long id : ids){
            tableMapper.deleteDbTables(id);
            columnMapper.deleteDbColumns(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void synTable(DbInfo dbInfo) throws Exception {
        Asserts.isTrue(tableMapper.countTableModels(dbInfo.getId()) == 0, "存在已绑定模型的表结构，不可以覆盖");
        dbMapper.updateSynInfo(dbInfo);
        indexMapper.deleteDbIndex(dbInfo.getId());
        columnMapper.deleteDbColumns(dbInfo.getId());
        tableMapper.deleteDbTables(dbInfo.getId());
        sequenceMapper.deleteDbSequence(dbInfo.getId());
        if ("oscar".equals(dbInfo.getDbType())) {
            synOscar(dbInfo);
        }else if ("mysql".equals(dbInfo.getDbType())) {
            synMysql(dbInfo);
        }else if("postgresql".equals(dbInfo.getDbType())){
            synPostgresql(dbInfo);
        }else{
            throw new AssertsException("不支持的数据库类型[" + dbInfo.getDbType() + "]");
        }
    }

    @Override
    public Map<String, String> preview(Long id) {
        VelocityContext velocityContext = prepareVelocity(id);
        Map<String, String> velocityMap = new LinkedHashMap<>();
        List<String> templates = templateList();
        for (String template : templates) {
            StringWriter stringWriter = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(velocityContext, stringWriter);
            velocityMap.put(template, stringWriter.toString());
            IOUtils.closeQuietly(stringWriter);
        }
        return velocityMap;
    }

    @Override
    public byte[] template(Long id) throws IOException {
        Map<String, String> map = preview(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for(Map.Entry<String, String> entry : map.entrySet()){
            zip.putNextEntry(new ZipEntry(getTemplatePath(entry.getKey())));
            IOUtils.write(entry.getValue(), zip, "UTF-8");
            zip.flush();
            zip.closeEntry();
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private String getTemplatePath(String templateName){
        if(templateName.endsWith("mysql.sql.vm")){
            return  "sql/mysql.sql";
        } else if (templateName.endsWith("oscar.sql.vm")){
            return  "sql/oscar.sql";
        } else if (templateName.endsWith("postgres.sql.vm")){
            return  "sql/postgres.sql";
        }
        return "null";
    }

    private VelocityContext prepareVelocity(Long id) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init(properties);

        DbTable param = new DbTable();
        param.setDbId(id);
        List<DbTable> tableList = tableMapper.list(param);

        List<VelocityTable> list = new ArrayList<>();
        for(DbTable table : tableList){
            VelocityTable velocityTable = new VelocityTable();
            velocityTable.setTableName(table.getTableName());
            velocityTable.setTableComment(table.getTableComment());
            velocityTable.setColumns(columnMapper.list(table.getId()));

            List<DbTableIndex> indexs = indexMapper.list(table.getId());
            List<DbTableIndex> merges = new ArrayList<>();
            DbTableIndex index = null;
            for(DbTableIndex i : indexs){
                if(index == null || !index.getIndexName().equals(i.getIndexName())){
                    index = i;
                    merges.add(index);
                }
                index.getColumns().add(i.getColumnName());
            }
            velocityTable.setIndexs(merges);
            list.add(velocityTable);
        }

        List<DbSequence> sequences = sequenceMapper.list(id);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tables", list);
        velocityContext.put("sequences", sequences);
        return velocityContext;
    }

    private List<String> templateList() {
        List<String> templates = new ArrayList<>();
        templates.add("vm/db/oscar.sql.vm");
        templates.add("vm/db/mysql.sql.vm");
        templates.add("vm/db/postgres.sql.vm");
        return templates;
    }

    private void synOscar(DbInfo dbInfo) throws Exception {
        try (SqlSession sqlSession = getSqlSession(
                "com.oscar.Driver", dbInfo.getDbUrl(), dbInfo.getDbUser(), dbInfo.getDbPasswd())) {
            String owner = dbInfo.getDbUser().toUpperCase();

            // 主键字段
            Map<String, List<String>> pkMap = new HashMap<>();
            List<DbTableColumn> primarys = sqlSession.selectList("oscarPrimarys", owner);
            for(DbTableColumn column : primarys){
                List<String> list = pkMap.computeIfAbsent(column.getTableName().toLowerCase(), k -> new ArrayList<>());
                list.add(column.getColumnName());
            }

            // 序列信息
            List<DbSequence> sequenceList = sqlSession.selectList("oscarSequences", owner);
            Map<String, DbSequence> sequenceMap = new HashMap<>();
            for(DbSequence sequence : sequenceList){
                sequence.setSequenceName(sequence.getSequenceName().toLowerCase());
                sequence.setDbId(dbInfo.getId());
                sequenceMap.put(sequence.getSequenceName().toLowerCase(), sequence);
            }

            // 库表信息
            Map<String, Long> tableMap = new HashMap<>();
            List<DbTable> tables = sqlSession.selectList("oscarTables", owner);
            for(DbTable table : tables){
                String tableName = table.getTableName().toLowerCase();
                table.setDbId(dbInfo.getId());
                table.setTableName(tableName);
                table.setAccessUserName(Access.userName());
                table.setAccessTime(table.getUpdateTime());
                tableMapper.insert(table);
                tableMap.put(tableName, table.getId());

                List<String> pkColumns = pkMap.get(tableName);
                Map<String, String> parmeter = new HashMap<>();
                parmeter.put("owner", owner);
                parmeter.put("tableName", tableName.toUpperCase());
                List<DbTableColumn> columns = sqlSession.selectList("oscarTableColumns", parmeter);
                for(DbTableColumn column : columns) {
                    if(pkColumns != null && pkColumns.contains(column.getColumnName())){
                        column.setIsPrimary(1);
                        // 如果是唯一的主键字段，并且有这个表的序列信息，那么认为是自增（没办法获取不到字段的自增属性）
                        if(pkColumns.size() == 1 && sequenceMap.containsKey(tableName + "_sequence")){
                            column.setIsIncrement(1);
                        }
                    }else{
                        column.setIsPrimary(0);
                    }
                    column.setTableId(table.getId());
                    column.setAccessUserName(Access.userName());
                    String columnDefault = column.getColumnDefault();
                    if(columnDefault != null && columnDefault.equalsIgnoreCase("null")){
                        column.setColumnDefault(null);
                    }
                    column.parseOscarColumn();
                    columnMapper.insert(column);
                }
            }
            for(DbSequence sequence : sequenceMap.values()){
                sequenceMapper.insert(sequence);
            }

            // 索引信息
            List<DbTableIndex> indexList = sqlSession.selectList("oscarIndexs", owner);
            for(DbTableIndex index : indexList){
                if(index.getIsPrimary() == 1){
                    continue;
                }
                index.setTableName(index.getTableName().toLowerCase());
                index.setColumnName(index.getColumnName().toLowerCase());
                index.setIndexName(index.getIndexName().toLowerCase());
                index.setTableId(tableMap.get(index.getTableName().toLowerCase()));
                indexMapper.insert(index);
            }
        }
    }

    private void synMysql(DbInfo dbInfo) throws Exception {
        try(SqlSession sqlSession = getSqlSession(
                "com.mysql.cj.jdbc.Driver", dbInfo.getDbUrl(), dbInfo.getDbUser(), dbInfo.getDbPasswd())){
            List<DbTable> tables = sqlSession.selectList("mysqlTables");
            for(DbTable table : tables){
                table.setDbId(dbInfo.getId());
                table.setAccessUserName(Access.userName());
                table.setAccessTime(table.getUpdateTime());
                tableMapper.insert(table);
                List<DbTableColumn> columns = sqlSession.selectList("mysqlTableColumns", table.getTableName());
                for(DbTableColumn column : columns){
                    column.setTableId(table.getId());
                    column.setAccessUserName(Access.userName());
                    column.parseMysqlColumn();
                    columnMapper.insert(column);
                }
            }
        }
    }

    private void synPostgresql(DbInfo dbInfo) throws Exception {
        try(SqlSession sqlSession = getSqlSession(
                "org.postgresql.Driver", dbInfo.getDbUrl(), dbInfo.getDbUser(), dbInfo.getDbPasswd())){
            List<DbTable> tables = sqlSession.selectList("postgresTables");
            for(DbTable table : tables) {
                table.setDbId(dbInfo.getId());
                table.setAccessUserName(Access.userName());
                tableMapper.insert(table);
                List<DbTableColumn> columns = sqlSession.selectList("postgresTableColumns", table.getTableName());
                for(DbTableColumn column : columns){
                    column.setTableId(table.getId());
                    column.setAccessUserName(Access.userName());
                    column.parsePostgresColumn();
                    columnMapper.insert(column);
                }
            }
        }
    }

    private SqlSession getSqlSession(String driver, String url, String user, String passwd) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("driver", driver);
        properties.setProperty("url", url);
        properties.setProperty("username", user);
        properties.setProperty("password", passwd);
        UnpooledDataSourceFactory factory = new UnpooledDataSourceFactory();
        factory.setProperties(properties);
        // DataSource
        DataSource dataSource = factory.getDataSource();
        // SqlSessionFactoryBean
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Resource[] resources = new Resource[]{new ClassPathResource("com/cowave/sys/meter/mapper/DbMapper.xml")};
        sqlSessionFactoryBean.setMapperLocations(resources);
        // SqlSessionFactory
        SqlSessionFactory sessionFactory = sqlSessionFactoryBean.getObject();
        // SqlSessionTemplate
        assert sessionFactory != null;
        return sessionFactory.openSession();
    }
}
