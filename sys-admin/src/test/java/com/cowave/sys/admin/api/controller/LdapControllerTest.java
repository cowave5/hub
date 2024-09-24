/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.controller;

import com.cowave.commons.framework.filter.access.AccessFilter;
import com.cowave.commons.framework.filter.security.TokenAuthenticationFilter;
import com.cowave.sys.admin.SpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shanhuiming
 *
 */
public class LdapControllerTest extends SpringTest {

    @Autowired
    private LdapController ldapController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(ldapController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, messageHelper))
                .addFilter(new TokenAuthenticationFilter(tokenService))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/ldap/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/ldap/info/1");
    }

    /**
     * 新增
     */
    @Test
    @Rollback()
    @Transactional
    public void add() throws Exception {
        mockPost("/api/v1/ldap/add", "{\"ldapName\":\"测试Ldap\"}");
    }

    /**
     * 编辑
     */
    @Test
    @Rollback()
    @Transactional
    public void edit() throws Exception {
        mockPost("/api/v1/ldap/edit", "{\"id\":2,\"ldapName\":\"测试Ldap\"}");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/ldap/delete?id=2");
    }

    /**
     * 修改状态
     */
    @Test
    public void changeStatus() throws Exception {
        mockGet("/api/v1/ldap/changeStatus?id=2&status=0");
    }

    /**
     * 用户信息
     */
    @Test
    public void ldapUser() throws Exception {
        mockGet("/api/v1/ldap/user/1");
    }
}
