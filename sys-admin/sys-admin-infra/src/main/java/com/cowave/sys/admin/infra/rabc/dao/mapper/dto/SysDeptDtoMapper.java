/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.base.vo.TreeChildren;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.dto.*;
import com.cowave.sys.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.sys.admin.domain.rabc.request.DeptQuery;
import com.cowave.sys.admin.domain.rabc.request.DeptUserQuery;
import com.cowave.sys.admin.domain.rabc.vo.DiagramNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysDeptDtoMapper {

    /**
     * 获取用户默认部门
     */
    SysDept getPrimaryDeptByUserId(Integer userId);

    /**
     * 列表
     */
    List<DeptListDto> list(@Param("tenantId") String tenantId, @Param("query") DeptQuery query);

    /**
     * 详情
     */
    DeptInfoDto info(@Param("tenantId") String tenantId, @Param("deptId") Integer deptId);

    /**
     * 部门岗位列表（已设置）
     */
    Page<DeptPostDto> getConfiguredPosts(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<DeptPostDto> page);

    /**
     * 获取部门岗位（未设置）
     */
    Page<DeptPostDto> getUnConfiguredPosts(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<DeptPostDto> page);

    /**
     * 获取部门成员（已加入）
     */
    Page<DeptUserDto> getJoinedMembers(@Param("tenantId") String tenantId, @Param("query") DeptUserQuery query, Page<RoleUserDto> page);

    /**
     * 获取部门成员（未加入）
     */
    Page<DeptUserDto> getUnJoinedMembers(@Param("tenantId") String tenantId, @Param("query") DeptUserQuery query, Page<RoleUserDto> page);

    /**
     * 下级部门id列表
     */
    List<Integer> childIds(Integer deptId);

    /**
     * 树
     */
    List<DiagramNode> listDiagramNodes(String tenantId);

    /**
     * 插入部门岗位
     */
    List<SysDeptPost> insertDeptPosts(@Param("tenantId") String tenantId, @Param("list") List<SysDeptPost> list);

    /**
     * 插入部门人员
     */
    void insertDeptUsers(@Param("tenantId") String tenantId, @Param("list") List<SysUserDept> list);

    /**
     * 默认岗位只允许一个
     */
    List<Integer> deptWithMultiDefaultPost();

    /**
     * 部门流程候选人
     */
    List<UserNameDto> getCandidatesByCode(@Param("tenantId") String tenantId, String deptCode);

    /**
     * 部门岗位选项
     */
    List<TreeChildren> listPostDiagramNode(String tenantId);

    /**
     * 部门用户树
     */
    List<TreeChildren> listUserDiagramNode(String tenantId);
}
