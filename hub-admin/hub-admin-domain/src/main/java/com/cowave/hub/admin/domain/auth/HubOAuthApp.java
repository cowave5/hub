/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.framework.support.mybatis.pg.PgListHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@TableName(value = "hub_oauth_app", autoResultMap = true)
public class HubOAuthApp implements AccessInfoSetter {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 客户端名称
     */
    @NotBlank(message = "{admin.oauth.name.null}")
    private String clientName;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 支持的授权方式
     */
    @NotBlank(message = "{admin.oauth.grant.null}")
    @TableField(typeHandler = PgListHandler.class)
    private List<String> grantType;

    /**
     * 授权范围
     */
    @NotBlank(message = "{admin.oauth.scope.null}")
    @TableField(typeHandler = PgListHandler.class)
    private List<String> authScope;

    /**
     * 回调地址
     */
    @NotBlank(message = "{admin.oauth.redirect.null}")
    private String redirectUrl;

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
