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
import com.cowave.hub.admin.domain.auth.dto.UserProfile;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.domain.rabc.dto.TenantManager;
import com.cowave.hub.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.UserListDto;
import com.cowave.hub.admin.domain.rabc.dto.UserNameDto;
import com.cowave.hub.admin.domain.rabc.request.TenantManagerRemove;
import com.cowave.hub.admin.domain.rabc.request.UserQuery;
import com.cowave.hub.admin.domain.rabc.vo.UserDiagramNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 *
 * @author shanhuiming
 */
@Mapper
public interface HubUserDtoMapper {

    /**
     * 列表
     */
    List<UserListDto> list(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 计数
     */
    int count(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 单位用户列表
     */
    List<UserListDto> listOfDept(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 单位用户计数
     */
    int countOfDept(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 详情
     */
    UserInfoDto getById(@Param("tenantId") String tenantId, @Param("userId") Integer userId);

    /**
     * 下级用户id
     */
    List<Integer> childIds(Integer userId);

    /**
     * 导入用户
     */
    void batchInsert(@Param("list") List<HubUser> list, @Param("overwrite") boolean overwrite);

    /**
     * 人员关系
     */
    List<UserDiagramNode> listDiagramNodes(String tenantId);

    /**
     * 用户流程候选人
     */
    List<UserNameDto> getUserCandidates(@Param("tenantId") String tenantId, @Param("userId") Integer userId);

    /**
     * 租户管理员列表
     */
    Page<TenantManager> listTenantManager(@Param("tenantId") String tenantId, Page<TenantManager> page);

    /**
     * 移除租户管理员
     */
    void removeTenantManager(TenantManagerRemove managerRemove);

    /**
     * 用户个人信息
     */
    UserProfile getUserProfile(Integer userId);
}
