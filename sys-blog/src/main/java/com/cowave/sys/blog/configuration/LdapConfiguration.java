/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@ConfigurationProperties(prefix = "ldap")
@Configuration
@Data
public class LdapConfiguration {

    /**
     * 用户名
     */
    private String ldapUser = "zhangyuliang@cowave.com";

    /**
     * 用户密码
     */
    private String ldapPasswd = "Cowave@123";

    /**
     * 基本DN
     */
    private String baseDn = "OU=Cowavers,DC=cowave,DC=com";

    /**
     * 用户DN
     */
    private String userDn = "";

    /**
     * Ldap地址
     */
    private String ldapUrl = "ldap://10.64.3.1:389";

    /**
     * 是否以匿名身份只读
     */
    private int readonly;

    /**
     * 环境属性
     */
    private Map<String, Object> environment;

    public String[] determineUrls() {
        return new String[]{this.ldapUrl};
    }

    public boolean anonymousReadOnly(){
        return readonly == 1;
    }
}
