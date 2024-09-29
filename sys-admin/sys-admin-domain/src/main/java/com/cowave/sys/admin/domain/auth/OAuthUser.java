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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

import static com.cowave.sys.admin.domain.rabc.enums.AccessType.GITLAB;

/**
 * 授权用户
 */
@Data
@TableName("oauth_user")
public class OAuthUser {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务类型
     */
    private String serverType;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户角色
     */
    private String roleCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime = new Date();

    public AccessUserDetails newUserDetails(){
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setType(GITLAB.val());
        userDetails.setUserId(id);
        userDetails.setUserCode(userCode);
        userDetails.setUsername(userAccount);
        userDetails.setUserNick(userName);
        userDetails.setDeptName(userDept);
        return userDetails;
    }
}
