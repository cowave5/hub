/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.oauth;

import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 * @author shanhuiming
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OAuthConfig extends AccessUser {

    /**
     * Gitlab
     */
    public static final String APP_GITLAB = "gitlab";

    /**
     * Gitlab auth api
     */
    public static final String GITLAB_AUTH = "/oauth/authorize";

    /**
     * Gitlab token api
     */
    public static final String GITLAB_TOKEN = "/oauth/token";

    /**
     * Gitlab user api
     */
    public static final String GITLAB_USER = "/api/v4/user";

    /**
     * id
     */
    private Integer id;

    /**
     * 状态
     */
    private int status;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 授权服务url
     */
    private String authUrl;

    /**
     * 应用回调地址
     */
    private String redirectUrl;

    /**
     * 授权方式
     */
    private String grantType = "authorization_code";

    /**
     * 授权范围
     */
    private String authScope = "read_user";

    /**
     * Token响应类型
     */
    private String responseType = "code";

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新部门
     */
    private Long updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String oauthUrl(){
        if(APP_GITLAB.equals(appType)){
            return gitlabAuthUrl();
        }
        return null;
    }

    public String gitlabAuthUrl(){
        return authUrl + GITLAB_AUTH
                + "?client_id=" + appId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=" + responseType;
    }

    public String gitlabTokenUrl(String code){
        return authUrl + GITLAB_TOKEN
                + "?client_id=" + appId
                + "&client_secret=" + appSecret
                + "&redirect_uri=" + redirectUrl
                + "&grant_type=" + grantType
                + "&scope=" + authScope
                + "&code=" + code;
    }

    public String gitlabUserUrl(){
        return authUrl + GITLAB_USER;
    }
}
