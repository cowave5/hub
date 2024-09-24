/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.entity;

import com.cowave.commons.framework.access.AccessUser;
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
public class ModelInfo extends AccessUser {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
