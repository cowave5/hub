/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.sys.admin.domain.rabc.SysUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.cowave.sys.admin.domain.auth.AuthType.SYS;

/**
 * @author shanhuiming
 */
@Data
public class TenantManagerCreate {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    private String tenantId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * 用户密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{admin.user.passwd.null}")
    private String userPasswd;

    public SysUser newSysUser() {
        SysUser sysUser = new SysUser();
        sysUser.setUserType(SYS.val());
        sysUser.setTenantId(tenantId);
        sysUser.setUserName(userName);
        sysUser.setUserAccount(userAccount);
        return sysUser;
    }
}
