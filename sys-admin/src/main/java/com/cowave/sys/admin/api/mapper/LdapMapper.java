/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import com.cowave.sys.admin.api.entity.ldap.LdapConfig;
import com.cowave.sys.admin.api.entity.ldap.LdapUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface LdapMapper {

    /**
     * 获取启用的ldap配置，只取一个
     */
    LdapConfig getEnableLdap();

    /**
     * 列表
     */
    List<LdapConfig> list(LdapConfig ldapConfig);

    /**
     * 详情
     */
    LdapConfig info(int id);

    /**
     * 新增
     */
    void insert(LdapConfig ldapConfig);

    /**
     * 更新
     */
    void update(LdapConfig ldapConfig);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * Ldap启用计数
     */
    int enableCount(int id);

    /**
     * 修改状态
     */
    void changeStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 创建系统用户
     */
    void createSysUser(LdapUser ldapUser);

    /**
     * 赋予系统角色
     */
    void grantSysRole(@Param("userId") long userId, @Param("roleId") long roleId);

    /**
     * 保存Ldap用户
     */
    void saveLdapUser(LdapUser ldapUser);

    /**
     * 获取用户Ldap信息
     */
    LdapUser ldapUserInfo(Long userId);
}
