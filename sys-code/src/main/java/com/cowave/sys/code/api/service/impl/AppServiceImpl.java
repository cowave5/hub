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
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.code.api.entity.AppInfo;
import com.cowave.sys.code.api.entity.ModelInfo;
import com.cowave.sys.code.api.mapper.AppMapper;
import com.cowave.sys.code.api.service.AppService;
import com.cowave.sys.code.api.service.DbService;
import com.cowave.sys.code.api.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AppServiceImpl implements AppService {

    private final AppMapper appMapper;

    private final ModelService modelService;

    private final DbService dbService;

    @Override
    public List<AppInfo> list(AppInfo appInfo) {
        return appMapper.list(appInfo);
    }

    @Override
    public AppInfo info(Long id) {
        AppInfo appInfo = appMapper.info(id);
        if(appInfo != null){
            appInfo.setModelList(appMapper.modelList(id));
        }
        return appInfo;
    }

    @Override
    public void add(AppInfo appInfo) {
        appMapper.insert(appInfo);
        if(CollectionUtils.isNotEmpty(appInfo.getModelList())){
            appMapper.insertModels(appInfo.getId(), appInfo.getModelList());
        }
    }

    @Override
    public void edit(AppInfo appInfo) {
        appMapper.update(appInfo);
        appMapper.deleteModels(appInfo.getId());
        if(CollectionUtils.isNotEmpty(appInfo.getModelList())){
            appMapper.insertModels(appInfo.getId(), appInfo.getModelList());
        }
    }

    @Override
    public void delete(Integer[] ids) {
        appMapper.delete(ids);
    }

    @Override
    public byte[] template(Long id) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 固定文件
        zipFile("vm/static/install.sh", "/bin/install.sh", zip);
        zipFile("vm/static/run.sh", "/bin/run.sh", zip);
        zipFile("vm/static/setenv.sh", "/bin/setenv.sh", zip);
        zipFile("vm/static/gitignore", "/gitignore", zip);
        zipFile("vm/static/docker.build", "/docker.build", zip);
        zipFile("vm/static/application.yml", "/src/main/resources/config/application.yml", zip);
        zipFile("vm/static/logback-spring.xml", "/src/main/resources/logback-spring.xml", zip);

        AppInfo appInfo = info(id);
        zipApiCode(zip, appInfo, appMapper.modelList(id));
        zipAppConfig(zip, appInfo);
        zipSql(zip, appInfo);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private void zipFile(String file, String entry, ZipOutputStream zip) throws IOException {
        try(InputStream inputStream = new ClassPathResource(file).getInputStream()){
            zip.putNextEntry(new ZipEntry(entry));
            IOUtils.copy(inputStream, zip);
            zip.flush();
            zip.closeEntry();
        }
    }

    private void zipApiCode(ZipOutputStream zip, AppInfo appInfo, List<Long> models) throws IOException {
        for(Long modelId : models){
            ModelInfo model = modelService.info(modelId);
            model.setHttpContext(appInfo.getHttpContext());
            model.setAppCode(appInfo.getAppCode());
            model.setAppPackage("com.cowave." + appInfo.getProjectCode() + "." + appInfo.getAppCode());
            Map<String, String> map  = modelService.preview(model);
            for(Map.Entry<String, String> entry : map.entrySet()){
                zip.putNextEntry(new ZipEntry(getApiCodePath(entry.getKey(), model)));
                IOUtils.write(entry.getValue(), zip, "UTF-8");
                zip.flush();
                zip.closeEntry();
            }
        }
    }

    private String getApiCodePath(String templateName, ModelInfo model){
        String javaPath = "/src/main/java/"
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
            return "views/api/" + model.getClassName() + ".js";
        } else if (templateName.endsWith("index.vue.vm")) {
            return "views/" + model.getClassName() + ".vue";
        }
        return "null";
    }

    private void zipAppConfig(ZipOutputStream zip, AppInfo appInfo) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init(properties);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("package", "com.cowave." + appInfo.getProjectCode() + "." + appInfo.getAppCode());
        velocityContext.put("commentDate", DateUtils.format("yyyy/MM/dd"));
        velocityContext.put("commentAuthor", Access.userAccount());
        velocityContext.put("projectCode", appInfo.getProjectCode());
        velocityContext.put("appCode", appInfo.getAppCode());
        velocityContext.put("version", appInfo.getAppVersion());
        velocityContext.put("appName", appInfo.getAppName());
        velocityContext.put("appDesc", appInfo.acquireAppDesc());
        velocityContext.put("isDb", appInfo.getDbId() != null);
        velocityContext.put("dbType", appInfo.getDbType());
        velocityContext.put("dbUrl", appInfo.getDbUrl());
        velocityContext.put("dbUser", appInfo.getDbUser());
        velocityContext.put("dbPasswd", appInfo.getDbPasswd());
        velocityContext.put("isSecurity", appInfo.getIsSecurity());
        velocityContext.put("httpPort", appInfo.getHttpPort());
        velocityContext.put("httpContext", appInfo.getHttpContext());

        valueTemplate("vm/app/pom.xml.vm", "/pom.xml", zip, velocityContext);
        valueTemplate("vm/app/info.yml.vm", "/src/main/resources/META-INF/info.yml", zip, velocityContext);
        valueTemplate("vm/app/README.md.vm", "/README.md", zip, velocityContext);
        valueTemplate("vm/app/smart-doc.json.vm", "/src/main/resources/smart-doc.json", zip, velocityContext);
        valueTemplate("vm/app/env.properties.vm", "/bin/env.properties", zip, velocityContext);
        valueTemplate("vm/app/dev.yml.vm", "/src/main/resources/config/dev/application.yml", zip, velocityContext);
        valueTemplate("vm/app/prod.yml.vm", "/src/main/resources/config/prod/application.yml", zip, velocityContext);

        String applicationJava = "/src/main/java/com/cowave/" + appInfo.getProjectCode() + "/" + appInfo.getAppCode() + "/Application.java";
        valueTemplate("vm/app/application.java.vm", applicationJava, zip, velocityContext);
        if(appInfo.getIsSecurity() == 1){
            String securityConfiguration = "/src/main/java/com/cowave/" + appInfo.getProjectCode() + "/" + appInfo.getAppCode() + "/security/SecurityConfiguration.java";
            valueTemplate("vm/app/security.java.vm", securityConfiguration, zip, velocityContext);
        }
    }

    private void valueTemplate(String templatePath, String zipPath, ZipOutputStream zip, VelocityContext velocityContext) throws IOException {
        try(StringWriter stringWriter = new StringWriter()){
            Template template = Velocity.getTemplate(templatePath, "UTF-8");
            template.merge(velocityContext, stringWriter);
            zip.putNextEntry(new ZipEntry(zipPath));
            IOUtils.write(stringWriter.toString(), zip, "UTF-8");
            zip.flush();
            zip.closeEntry();
        }
    }

    private void zipSql(ZipOutputStream zip, AppInfo appInfo) throws IOException {
        Long dbId = appInfo.getDbId();
        if(dbId == null){
            return;
        }
        zipFile("vm/static/changelog.yml", "/src/main/resources/sql/changelog.yml", zip);
        Map<String, String> sqlMap = dbService.preview(dbId);
        String sql = "";
        if("oscar".equals(appInfo.getDbType())){
            sql = sqlMap.get("vm/db/oscar.sql.vm");
        }else if("mysql".equals(appInfo.getDbType())){
            sql = sqlMap.get("vm/db/mysql.sql.vm");
        }else if("postgresql".equals(appInfo.getDbType())){
            sql = sqlMap.get("vm/db/postgres.sql.vm");
        }
        zip.putNextEntry(new ZipEntry("/src/main/resources/sql/init.sql"));
        IOUtils.write(sql, zip, "UTF-8");
        zip.flush();
        zip.closeEntry();
    }
}
