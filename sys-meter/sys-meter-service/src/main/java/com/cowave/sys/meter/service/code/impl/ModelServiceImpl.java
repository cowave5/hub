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

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.meter.domain.code.DbTableColumn;
import com.cowave.sys.meter.domain.code.ModelField;
import com.cowave.sys.meter.domain.code.ModelInfo;
import com.cowave.sys.meter.domain.code.SelectOption;
import com.cowave.sys.meter.infra.code.dao.mapper.dto.ColumnMapper;
import com.cowave.sys.meter.infra.code.dao.mapper.dto.FieldMapper;
import com.cowave.sys.meter.infra.code.dao.mapper.dto.ModelMapper;
import com.cowave.sys.meter.service.code.ModelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ModelServiceImpl implements ModelService {

    private final ModelMapper modelMapper;

    private final FieldMapper fieldMapper;

    private final ColumnMapper columnMapper;

    @Override
    public List<SelectOption> options() {
        return modelMapper.options();
    }

    @Override
    public List<ModelInfo> list(ModelInfo modelInfo) {
        return modelMapper.list(modelInfo);
    }

    @Override
    public ModelInfo info(Long id) {
        return modelMapper.info(id);
    }

    @Override
    public void add(ModelInfo modelInfo) {
        modelMapper.insert(modelInfo);
    }

    @Override
    public void edit(ModelInfo modelInfo) {
        modelMapper.update(modelInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer[] id) {
        modelMapper.delete(id);
        fieldMapper.clearModelFields(id);
    }

    @Override
    public void switchExcel(Long id, Integer flag) {
        modelMapper.switchExcel(id, flag, Access.accessInfo());
    }

    @Override
    public void switchAccess(Long id, Integer flag) {
        modelMapper.switchAccess(id, flag, Access.accessInfo());
    }

    @Override
    public void switchLog(Long id, Integer flag) {
        modelMapper.switchLog(id, flag, Access.accessInfo());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generate(ModelInfo modelInfo) {
        modelMapper.insert(modelInfo);
        List<DbTableColumn> columns = columnMapper.list(modelInfo.getTableId());
        for(DbTableColumn column : columns){
            ModelField field = column.toField();
            field.setModelId(modelInfo.getId());
            fieldMapper.insert(field);
        }
    }
    @Override
    public Map<String, String> preview(Long id){
        return preview(modelMapper.info(id));
    }
    @Override
    public Map<String, String> preview(ModelInfo model) {
        List<ModelField> fields = fieldMapper.list(model.getId());
        VelocityContext velocityContext = prepareJavaVelocity(model, fields);
        Map<String, String> javaMap = new LinkedHashMap<>();
        List<String> templates = templateList();
        for (String template : templates) {
            StringWriter stringWriter = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(velocityContext, stringWriter);
            javaMap.put(template, stringWriter.toString());
            IOUtils.closeQuietly(stringWriter);
        }
        return javaMap;
    }

    @Override
    public byte[] template(Long id) throws IOException {
        Map<String, String> map = preview(id);
        ModelInfo model = modelMapper.info(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for(Map.Entry<String, String> entry : map.entrySet()){
            zip.putNextEntry(new ZipEntry(getTemplatePath(entry.getKey(), model)));
            IOUtils.write(entry.getValue(), zip, "UTF-8");
            zip.flush();
            zip.closeEntry();
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private String getTemplatePath(String templateName, ModelInfo model){
        String javaPath = "main/java" + "/"
                + StringUtils.replace(model.getAppPackage(), ".", "/") + "/api";
        if(templateName.endsWith("entity.java.vm")){
            return javaPath + "/entity/" + model.getClassName() + ".java";
        } else if (templateName.endsWith("controller.java.vm")) {
            return javaPath + "/controller/" + model.getClassName() + "Controller.java";
        } else if (templateName.endsWith("service.java.vm")) {
            return javaPath + "/service/" + model.getClassName() + "Service.java";
        } else if (templateName.endsWith("serviceImpl.java.vm")) {
            return javaPath + "/service/impl/" + model.getClassName() + "ServiceImpl.java";
        } else if (templateName.endsWith("mapper.java.vm")) {
            return javaPath + "/mapper/" + model.getClassName() + "Mapper.java";
        } else if (templateName.endsWith("mapper.xml.vm")) {
            return javaPath + "/mapper/" + model.getClassName() + "Mapper.xml";
        } else if (templateName.endsWith("api.js.vm")) {
            return "views/api.js";
        } else if (templateName.endsWith("index.vue.vm")) {
            return "views/index.vue";
        }
        return "null";
    }

    private VelocityContext prepareJavaVelocity(ModelInfo model, List<ModelField> fields) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init(properties);

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("package", model.getAppPackage());
        velocityContext.put("className", model.getClassName());
        velocityContext.put("classComment", model.acquireClassComment());
        velocityContext.put("modelName", model.acquireClassComment());
        velocityContext.put("instanceName", StringUtils.uncapitalize(model.getClassName()));
        velocityContext.put("commentAuthor", Access.userAccount());
        velocityContext.put("commentDate", DateUtils.format("yyyy/MM/dd"));
        velocityContext.put("logModel", model.getIsLog());
        velocityContext.put("logType", model.acquireLogType());

        String httpContext = model.getHttpContext();
        if('/' == httpContext.charAt(httpContext.length() - 1)){
            httpContext = httpContext.substring(0, httpContext.length() - 1);
        }
        velocityContext.put("httpContext", httpContext);

        String apiContext = model.getApiContext();
        if('/' == apiContext.charAt(apiContext.length() - 1)){
            apiContext = apiContext.substring(0, apiContext.length() - 1);
        }
        velocityContext.put("apiContext", apiContext);
        velocityContext.put("appCode", model.getAppCode());

        String authPrefix = model.getAuthPrefix();
        if(authPrefix != null && ':' == authPrefix.charAt(authPrefix.length() - 1)){
            authPrefix = authPrefix.substring(0, authPrefix.length() - 1);
        }
        velocityContext.put("authPrefix", authPrefix);
        velocityContext.put("excelModel", model.getIsExcel());
        velocityContext.put("accessModel", model.getIsAccess());
        velocityContext.put("tableName", model.getTableName());

        velocityContext.put("fields", fields);
        velocityContext.put("imports", fieldImports(fields));
        velocityContext.put("pk", fieldPrimary(fields));
        return velocityContext;
    }

    private ModelField fieldPrimary(List<ModelField> fields){
        for(ModelField field : fields) {
            if(field.getIsPrimary() == 1){
                return field;
            }
        }
        for(ModelField field : fields) {
            if(field.getColumnId() != null){
                return field;
            }
        }
        return null;
    }

    private Set<String> fieldImports(List<ModelField> fields){
        Set<String> imports = new TreeSet<>();
        for(ModelField field : fields) {
            if("Date".equals(field.getFieldTypeName())){
                imports.add("java.util.Date");
                imports.add("com.fasterxml.jackson.annotation.JsonFormat");

            }
            if("BigDecimal".equals(field.getFieldTypeName())){
                imports.add("java.math.BigDecimal");
            }
            if("Map<String, Object>".equals(field.getFieldTypeName())){
                imports.add("java.util.Map");
            }
            if(field.getIsCollect() == 1){
                imports.add("java.util.List");
            }
            if(field.getIsNotnull() == 1 && (field.getIsIncrement() == null || field.getIsIncrement() == 0)){
                imports.add("javax.validation.constraints.NotNull");
            }
            String converter = field.getExcelConverter();
            if(converter != null){
                imports.add(converter);
                converter = converter.substring(converter.lastIndexOf(".") + 1) + ".class";
                field.setExcelConverter(converter);
            }
        }
        return imports;
    }

    private List<String> templateList() {
        List<String> templates = new ArrayList<>();
        templates.add("vm/java/entity.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/mapper.xml.vm");
        templates.add("vm/vue/api.js.vm");
        templates.add("vm/vue/index.vue.vm");
        return templates;
    }
}
