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
import java.util.List;

/**
 * 应用
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AppInfo extends AccessUser {

    /**
     * id
     */
    private Long id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用名称版本
     */
    private String appVersion;

    /**
     * 应用说明
     */
    private String appDesc;

    /**
     * http端口
     */
    private Integer httpPort;

    /**
     * http路径
     */
    private String httpContext;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 数据库id
     */
    private Long dbId;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库实例
     */
    private String dbCode;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据库连接
     */
    private String dbUrl;

    /**
     * 数据库用户
     */
    private String dbUser;

    /**
     * 数据库密码
     */
    private String dbPasswd;

    /**
     * 支持鉴权
     */
    private Integer isSecurity;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 模型列表
     */
    private List<Long> modelList;

    public String acquireAppDesc(){
        if(appDesc == null){
            return "";
        }
        return appDesc;
    }
}
