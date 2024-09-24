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
public class DbInfo extends AccessUser {

    /**
     * id
     */
    private Long id;

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
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
