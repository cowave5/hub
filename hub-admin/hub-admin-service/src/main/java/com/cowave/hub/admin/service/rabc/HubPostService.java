/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rabc.HubPost;
import com.cowave.hub.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.UserNameDto;
import com.cowave.hub.admin.domain.rabc.request.DeptPostQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubPostService {

	/**
	 * 分页列表
	 */
	Page<HubPost> pageList(String tenantId, DeptPostQuery query);

	/**
	 * 列表
	 */
	List<HubPost> list(String tenantId, DeptPostQuery query);

	/**
	 * 详情
	 */
	PostInfoDto info(String tenantId, Integer postId);

	/**
	 * 新增
	 */
	void create(String tenantId, PostInfoDto post);

	/**
	 * 删除
	 */
	void delete(String tenantId, List<Integer> postIds);

	/**
	 * 修改
	 */
	void edit(String tenantId, PostInfoDto post);

	/**
	 * 岗位组织架构
	 */
	Tree<Integer> getDiagram(String tenantId);

	/**
	 * 岗位流程候选人
	 */
	List<UserNameDto> getCandidatesByCode(String tenantId, String postCode);

	/**
     * 岗位名称查询
     */
	String getNameById(String tenantId, Integer postId);

	/**
     * 部门岗位名称查询
     */
    List<String> getNameOfDeptPost(String tenantId, List<String> deptPosts);
}
