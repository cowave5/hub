/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.cowave.commons.framework.support.excel.valid.ExcelDataImporter;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysUserService extends ExcelDataImporter<SysUser> {

    /**
     * 列表
     */
    List<SysUser> list(SysUser sysUser);

    /**
     * 计数
     */
    int count(SysUser sysUser);

    /**
     * 详情
     */
    SysUser info(Long userId);

    /**
     * 新增
     */
    void add(SysUser sysUser);

    /**
     * 编辑
     */
    SysUser edit(SysUser sysUser);

    /**
     * 删除
     */
    List<SysUser> delete(Long[] userIds);

    /**
     * 修改状态
     */
    void changeStatus(SysUser sysUser);

    /**
     * 修改密码
     */
    void changePasswd(SysUser sysUser);

    /**
     * 修改角色
     */
    void changeRoles(SysUser sysUser);

    /**
     * 修改只读
     */
    void changeReadonly(SysUser sysUser);

    /**
     * 候选人: 用户Leaders
     */
    List<SysUser> leaders(Long userId);
}
