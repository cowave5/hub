/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rabc.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rabc.HubPost;
import com.cowave.hub.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.UserNameDto;
import com.cowave.hub.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.hub.admin.domain.rabc.vo.DiagramNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubPostDtoMapper {

    /**
     * 分页列表
     */
    Page<HubPost> pageList(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<HubPost> page);

    /**
	 * 列表
	 */
	List<HubPost> list(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query);

    /**
     * 详情
     */
    PostInfoDto info(@Param("tenantId") String tenantId, @Param("postId") Integer postId);

    /**
     * 岗位流程候选人
     */
    List<UserNameDto> getCandidatesByCode(@Param("tenantId") String tenantId, @Param("postCode") String postCode);

    /**
     * 下级岗位id列表
     */
    List<Integer> childIds(Integer postId);

    /**
     * 岗位组织
     */
    List<DiagramNode> listDiagramNodes(String tenantId);

    /**
     * 查询部门岗位名称
     */
    String getNameOfDeptPost(@Param("tenantId") String tenantId, @Param("deptId") Integer deptId, @Param("postId") Integer postId);
}
