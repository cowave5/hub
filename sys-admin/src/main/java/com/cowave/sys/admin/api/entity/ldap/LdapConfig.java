/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.ldap;

import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LdapConfig extends AccessUser {

    /**
     * id
     */
    private Integer id;

    /**
     * Ldap名称
     */
    private String ldapName;

    /**
     * Ldap状态
     */
    private int ldapStatus;

    /**
     * Ldap地址
     */
    private String ldapUrl;

    /**
     * 用户名
     */
    private String ldapUser;

    /**
     * 用户密码
     */
    private String ldapPasswd;

    /**
     * 基本DN
     */
    private String baseDn;

    /**
     * 是否以匿名身份只读
     */
    private int readonly;

    /**
     * 用户DN
     */
    private String userDn;

    /**
     * 用户对象类
     */
    private String userClass;

    /**
     * 用户默认角色
     */
    private Long userRole;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 环境属性
     */
    private Map<String, Object> environment;

    /**
     * 用户名属性
     */
    private String accountProperty;

    /**
     * 姓名属性
     */
    private String nameProperty;

    /**
     * 邮箱属性
     */
    private String emailProperty;

    /**
     * 电话属性
     */
    private String phoneProperty;

    /**
     * 岗位属性
     */
    private String postProperty;

    /**
     * 单位属性
     */
    private String deptProperty;

    /**
     * 上级领导属性
     */
    private String leaderProperty;

    /**
     * 用户信息属性
     */
    private String infoProperty;

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

    public String getUserDn(){
        if(userDn == null){
            return "";
        }
        return userDn;
    }

    public String[] determineUrls() {
        return new String[]{this.ldapUrl};
    }

    public boolean anonymousReadOnly(){
        return readonly == 1;
    }
}
