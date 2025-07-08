/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.sys.admin.domain.auth.LdapConfig;
import com.cowave.sys.admin.domain.auth.dto.LdapUserDto;

/**
 * @author shanhuiming
 */
public interface LdapService {

    /**
     * Ldap认证
     */
    AccessUserDetails authenticate(String tenantId, String userAccount, String passWord);

    /**
     * 获取配置
     */
    LdapConfig getConfig(String tenantId);

    /**
     * 修改配置
     */
    void updateConfig(String tenantId, LdapConfig ldapConfig);

    /**
     * 测试配置
     */
    void validConfig(LdapConfig ldapConfig);

    /**
     * 用户列表
     */
    Page<LdapUserDto> listUser(String tenantId, String ldapAccount);

    /**
     * 删除用户
     */
    void deleteUser(String tenantId, Integer userId);

    /**
     * 修改用户角色
     */
    void changeUserRole(String tenantId, Integer userId, String roleCode);
}
