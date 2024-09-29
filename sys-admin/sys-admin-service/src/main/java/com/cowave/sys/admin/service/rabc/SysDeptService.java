/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.dto.*;
import com.cowave.sys.admin.domain.rabc.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDeptService{

	/**
	 * 列表
	 */
	List<DeptListDto> list(DeptQuery query);

	/**
	 * 详情
	 */
	DeptInfoDto info(Integer deptId);

	/**
	 * 新增
	 */
	void create(DeptCreate dept);

	/**
	 * 删除
	 */
	void delete(List<Integer> deptIds);

	/**
	 * 修改
	 */
	void edit(DeptCreate dept);

	/**
	 * 导出列表
	 */
	List<SysDept> listForExport();

	/**
	 * 部门组织架构
	 */
	List<Tree<String>> getDiagram(String deptId);

	/**
     * 刷新部门组织
     */
    void refreshDiagram();

	/**
	 * 部门岗位树
	 */
	Tree<String> getPostDiagram();

	/**
	 * 部门用户树
	 */
	Tree<String> getUserDiagram();

	/**
	 * 添加部门岗位
	 */
	void addPosts(List<SysDeptPost> list);

	/**
     * 移除部门岗位
     */
	void removePosts(Integer deptId, List<Integer> postIds);

	/**
	 * 获取部门岗位（已设置）
	 */
	Page<DeptPostDto> getConfiguredPosts(DeptPostQuery query);

	/**
     * 获取部门岗位（未设置）
     */
    Page<DeptPostDto> getUnConfiguredPosts(DeptPostQuery query);

	/**
	 * 添加部门成员
	 */
	void addMembers(List<SysUserDept> list);

	/**
     * 移除部门成员
     */
	void removeMembers(Integer deptId, List<Integer> userIds);

	/**
	 * 获取部门成员（已加入）
	 */
	Page<DeptUserDto> getJoinedMembers(DeptUserQuery query);

	/**
	 * 获取部门成员（未加入）
	 */
	Page<DeptUserDto> getUnJoinedMembers(DeptUserQuery query);

	/**
	 * 部门流程候选人
	 */
	List<UserNameDto> getCandidatesByCode(String deptCode);

	/**
     * 部门名称查询
     */
    List<String> getNamesById(List<Integer> deptIds);
}
