/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.auth;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.auth.LdapConfig;
import com.cowave.sys.admin.domain.auth.dto.LdapUserDto;
import com.cowave.sys.admin.service.auth.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Ldap配置
 * @order 24
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ldap")
public class LdapController {

    private final LdapService ldapService;

    /**
     * 获取配置
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:query')")
    @GetMapping("/config/{ldapName}")
    public Response<LdapConfig> getConfig(@PathVariable String ldapName) {
        return Response.success(ldapService.queryByName(ldapName));
    }

    /**
     * 修改配置
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:edit')")
    @PatchMapping("/config")
    public Response<Void> updateConfig(@Validated @RequestBody LdapConfig ldapConfig) throws Exception {
        return Response.success(() -> ldapService.updateConfig(ldapConfig));
    }

    /**
     * 测试配置
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:edit')")
    @PostMapping("/config/valid")
    public Response<Void> validConfig(@Validated @RequestBody LdapConfig ldapConfig) throws Exception {
        return Response.success(() -> ldapService.validConfig(ldapConfig));
    }

    /**
     * 用户列表
     * @param ldapAccount ladp账号
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @GetMapping(value = {"/user"})
    public Response<Response.Page<LdapUserDto>> listUser(String ldapAccount) {
        return Response.page(ldapService.listUser(ldapAccount));
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @DeleteMapping(value = {"/user/{userId}"})
    public Response<Void> deleteUser(@PathVariable Integer userId) throws Exception {
        return Response.success(() -> ldapService.deleteUser(userId));
    }

    /**
     * 修改用户角色
     *
     * @param userId 用户id
     * @param roleCode 角色编码
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @PatchMapping("/user/role/{userId}/{roleCode}")
    public Response<Void> changeUserRole(@PathVariable Integer userId, @PathVariable String roleCode) throws Exception {
        return Response.success(() -> ldapService.changeUserRole(userId, roleCode));
    }
}
