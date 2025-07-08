/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain;

/**
 * @author shanhuiming
 */
public class AdminRedisKeys {

    /**
     * 验证码
     */
    public static final String AUTH_CAPTCHA = "sys-admin:auth:captcha:%s";

    /**
     * OAuth2授权code
     */
    public static final String AUTH_OAUTH = "sys-admin:auth:oauth:%s";

    /**
     * Api令牌
     */
    public static final String AUTH_API = "sys-admin:auth:api:%s";

    /**
     * Api令牌最近访问
     */
    public static final String AUTH_API_CURRENT = "sys-admin:auth:api:current:%s";

    /**
     * 账号登录锁定
     */
    public static final String LOGIN_LOCK = "sys-admin:auth:lock:%s";

    /**
     * 账号登录失败次数
     */
    public static final String LOGIN_FAILS = "sys-admin:auth:fails:%s";

    /**
     * 系统配置
     */
    public static final String CONFIG_KEY = "sys-admin:config";

    /**
     * 用户树
     */
    public static final String USER_DIAGRAM = "sys-admin:diagram:user";

    /**
     * 岗位树
     */
    public static final String POST_DIAGRAM = "sys-admin:diagram:post";

    /**
     * 部门树
     */
    public static final String DEPT_DIAGRAM = "sys-admin:diagram:dept";

    /**
     * 部门用户树
     */
    public static final String DEPT_USER_DIAGRAM = "sys-admin:diagram:dept-user";

    /**
     * 部门岗位树
     */
    public static final String DEPT_POST_DIAGRAM = "sys-admin:diagram:dept-post";

    /**
     * 字典
     */
    public static final String DICT_CODE = "sys-admin:dict:code";

    /**
     * 类型字典
     */
    public static final String DICT_TYPE = "sys-admin:dict:type";

    /**
     * 分组字典
     */
    public static final String DICT_GROUP = "sys-admin:dict:group";
}
