/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import lombok.Data;
import java.util.Date;

import static com.cowave.sys.admin.domain.auth.AccessType.LDAP;

/**
 * Ldap用户信息
 */
@Data
public class LdapUser {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户角色
     */
    private String roleCode;

    /**
     * Ldap名称
     */
    private String ldapName;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPasswd;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户岗位
     */
    private String userPost;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 上级用户
     */
    private String userLeader;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime;

    public AccessUserDetails newUserDetails(){
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setType(LDAP.val());
        userDetails.setUserId(id);
        userDetails.setUserCode(userCode);
        userDetails.setUsername(userAccount);
        userDetails.setUserNick(userName);
        userDetails.setDeptName(userDept);
        return userDetails;
    }
}
