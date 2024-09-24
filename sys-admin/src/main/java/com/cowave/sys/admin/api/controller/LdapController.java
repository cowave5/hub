/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.sys.admin.api.entity.ldap.LdapConfig;
import com.cowave.sys.admin.api.entity.ldap.LdapUser;
import com.cowave.sys.admin.api.service.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Ldap配置
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ldap")
public class LdapController {

    private final LdapService ldapService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:query')")
    @PostMapping("/list")
    public Response<Response.Page<LdapConfig>> list(@Validated @RequestBody LdapConfig ldapConfig) {
        return Response.page(ldapService.list(ldapConfig));
    }

    /**
     * 详情
     * @param id 配置id
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:query')")
    @GetMapping("/info/{id}")
    public Response<LdapConfig> info(@PathVariable Integer id) {
        return Response.success(ldapService.info(id));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:new')")
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody LdapConfig ldapConfig) {
        ldapService.add(ldapConfig);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:edit')")
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody LdapConfig ldapConfig) {
        ldapService.edit(ldapConfig);
        return Response.success();
    }

    /**
     * 删除
     * @param id 配置id
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:delete')")
    @GetMapping(value = {"/delete"})
    public Response<Void> delete(@NotNull(message = "user.notnull.id") Integer[] id) {
        ldapService.delete(id);
        return Response.success();
    }

    /**
     * 修改状态
     *
     * @param id 配置id
     * @param status 配置状态
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:edit')")
    @GetMapping("/changeStatus")
    public Response<Void> changeStatus(Integer id, Integer status) {
        ldapService.changeStatus(id, status);
        return Response.success();
    }

    /**
     * 测试
     */
    @PreAuthorize("@permit.hasPermit('sys:ldap:edit')")
    @PostMapping("/valid")
    public Response<Void> valid(@Validated @RequestBody LdapConfig ldapConfig) {
        ldapService.valid(ldapConfig);
        return Response.success();
    }

    /**
     * 用户信息
     * @param userId 用户id
     */
    @PreAuthorize("@permit.hasPermit('sys:user:query')")
    @GetMapping(value = {"/user/{userId}"})
    public Response<LdapUser> ldapUser(@PathVariable Long userId) {
        return Response.success(ldapService.userInfo(userId));
    }
}
