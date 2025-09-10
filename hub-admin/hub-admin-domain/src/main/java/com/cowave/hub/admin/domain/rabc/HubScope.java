/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.rabc;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.framework.support.mybatis.pg.PgJsonHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Data
@TableName(autoResultMap = true)
public class HubScope implements AccessInfoSetter {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 权限模块
     */
    private String scopeModule;

    /**
     * 权限名称
     */
    private String scopeName;

    /**
     * 权限状态
     */
    private Integer scopeStatus;

    /**
     * 权限内容
     */
    @TableField(typeHandler = PgJsonHandler.class)
    private Map<String, Object> scopeContent;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
