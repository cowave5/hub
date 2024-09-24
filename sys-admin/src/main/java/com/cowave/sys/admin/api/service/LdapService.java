/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.sys.admin.api.entity.ldap.LdapConfig;
import com.cowave.sys.admin.api.entity.ldap.LdapUser;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface LdapService {

    /**
     * Ldap认证
     */
    AccessToken authenticate(String userAccount, String passWord);

    /**
     * 列表
     */
    List<LdapConfig> list(LdapConfig ldapConfig);

    /**
     * 详情
     */
    LdapConfig info(Integer id);

    /**
     * 新增
     */
    void add(LdapConfig ldapConfig);

    /**
     * 修改
     */
    void edit(LdapConfig ldapConfig);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 修改状态
     */
    void changeStatus(Integer id, Integer status);

    /**
     * 测试Ldap
     */
    void valid(LdapConfig ldapConfig);

    /**
     * Ldap用户信息
     */
    LdapUser userInfo(Long userId);
}
