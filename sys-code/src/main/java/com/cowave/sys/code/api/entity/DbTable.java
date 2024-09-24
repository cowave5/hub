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
 * 表管理
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DbTable extends AccessUser {

    /**
     * 数据库id
     */
    private Long dbId;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库实例名
     */
    private String dbCode;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * id
     */
    private Long id;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
