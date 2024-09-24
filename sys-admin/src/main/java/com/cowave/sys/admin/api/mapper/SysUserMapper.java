/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import java.util.List;

import com.cowave.sys.admin.api.entity.TreeChildren;
import com.cowave.sys.admin.api.entity.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysUser;
import com.cowave.sys.model.admin.SysUserDept;

/**
 * 用户
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysUserMapper{

    /**
     * 部门用户树
     */
    List<TreeChildren> deptUserOptions();

	/**
	 * 账号查询
	 */
	SysUser queryByAccount(String userAccount);

    /**
     * 列表
     */
    List<SysUser> list(SysUser sysUser);

    /**
     * 计数
     */
    int count(SysUser sysUser);

    /**
     * 单位用户列表
     */
    List<SysUser> listWithDept(SysUser sysUser);

    /**
     * 单位用户计数
     */
    int countWithDept(SysUser sysUser);

    /**
     * 详情
     */
    SysUser info(long userId);

    /**
     * 新增
     */
    void insert(SysUser sysUser);

    /**
     * 更新
     */
    void update(SysUser sysUser);

    /**
     * 删除
     */
    void delete(long userId);

    /**
     * 插入用户角色
     */
    void insertUserRoles(@Param("userId") long userId, @Param("list") List<Long> list);

    /**
     * 插入用户部门
     */
    void insertUserDepts(List<SysUserDept> list);

    /**
     * 插入上级用户
     */
    void insertUserParents(@Param("userId") long userId, @Param("list") List<Long> list);

    /**
     * 删除上级用户
     */
    void deleteUserParents(long userId);

    /**
     * 删除下级用户
     */
    void deleteUserChilds(long userId);

    /**
     * 删除用户角色
     */
    void deleteUserRoles(long userId);

    /**
     * 删除用户部门
     */
    void deleteUserDepts(long userId);

    /**
     * 下级用户id列表
     */
    List<Long> childIds(long userId);

    /**
     * 账号计数
     */
    int countAccounts(@Param("userId") Long userId, @Param("userAccount") String userAccount);

    /**
     * 用户权限符
     */
    List<String> permitKeys(Long userId);

    /**
     * 修改状态
     */
    void changeStatus(SysUser sysUser);

    /**
     * 修改密码
     */
    void changePasswd(SysUser sysUser);

    /**
     * 修改只读
     */
    void changeReadonly(SysUser sysUser);

    /**
     * 导入用户
     */
    void batchInsert(@Param("list") List<SysUser> list, @Param("overwrite") boolean overwrite);

    /**
     * 人员关系
     */
    List<TreeNode> listTree();

    /**
     * id查询账号
     */
    String queryAccountById(Long userId);

    /**
     * id查询姓名
     */
    String queryNameById(Long userId);

    /**
     * 候选人: 用户Leaders
     */
    List<SysUser> leaders(long userId);
}
