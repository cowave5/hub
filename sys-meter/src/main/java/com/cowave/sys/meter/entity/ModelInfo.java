/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.entity;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 模型
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ModelInfo extends AccessInfo {

    /**
     * id
     */
    private Long id;

    /**
     * 类名
     */
    private String className;

    /**
     * 类注释
     */
    private String classComment;

    /**
     * Api路径
     */
    private String apiContext;

    /**
     * 权限前缀
     */
    private String authPrefix;

    /**
     * 支持Excel导出
     */
    private int isExcel;

    /**
     * 是否继承Access
     */
    private int isAccess;

    /**
     * 是否记录操作日志
     */
    private int isLog;

    /**
     * 日志记录类型【字典】
     */
    private String logType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 应用编码
     */
    private String appCode = "app";

    /**
     * 应该http路径
     */
    private String httpContext = "/app";

    /**
     * 应用package
     */
    private String appPackage = "com.cowave.project.app";

    public String acquireClassComment(){
        if(classComment == null){
            return className;
        }
        return classComment;
    }

    public String acquireLogType(){
        if(logType == null){
            return "log_" + className;
        }
        return logType;
    }
}
