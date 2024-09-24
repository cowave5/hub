/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.sys.admin.api.entity.ldap.LdapUser;
import com.cowave.sys.model.admin.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class UserProfile {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 旧密码
     */
    private String oldPasswd;

    /**
     * 新密码
     */
    private String newPasswd;

    /**
     * 用户电话
     */
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "user.invalid.phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "user.invalid.email")
    private String userEmail;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否OAuth用户
     */
    private int isoauth;

    /**
     * 部门/岗位
     */
    private List<String> depts = new ArrayList<>();

    /**
     * 上级用户
     */
    private List<String> parents = new ArrayList<>();

    /**
     * 用户角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 用户权限
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * ldap用户部门
     */
    private String ldapDept;

    /**
     * ldap用户岗位
     */
    private String ldapPost;

    /**
     * ldap用户上级
     */
    private String ldapLeader;

    public UserProfile(){

    }

    public UserProfile(AccessToken accessToken){
        this.roles = accessToken.getRoles();
        this.permissions = accessToken.getPermissions();
        this.userName = accessToken.getUserNick();
    }

    public List<String> getDepts(){
        if(SysUser.TYPE_LDAP == userType && depts.isEmpty()){
            return List.of("Ldap: " + ldapDept + "/" + ldapPost);
        }
        return depts;
    }

    public List<String> getParents(){
        if(SysUser.TYPE_LDAP == userType && parents.isEmpty()){
            return List.of("Ldap: " + ldapLeader);
        }
        return parents;
    }

    public void setLdapInfo(LdapUser ldapUser){
        if(ldapUser == null){
            return;
        }
        this.ldapPost = ldapUser.getUserPost();
        this.ldapDept = ldapUser.getUserDept();
        this.ldapLeader = ldapUser.getUserLeader();
    }
}
