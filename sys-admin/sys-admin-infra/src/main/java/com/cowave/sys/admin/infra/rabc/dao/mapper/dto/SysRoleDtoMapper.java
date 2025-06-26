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
import com.cowave.sys.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.sys.admin.domain.rabc.request.RoleUserQuery;
import com.cowave.sys.admin.domain.rabc.request.RoleUserUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色
 *
 * @author shanhuiming
 */
@Mapper
public interface SysRoleDtoMapper {

    /**
     * 获取用户角色
     */
    List<String> getRoleCodesByUserId(Integer userId);

    /**
     * 详情
     */
    RoleInfoDto info(@Param("tenantId") String tenantId, @Param("roleId") Integer roleId);

    /**
     * 授权用户
     */
    void addRoleUser(@Param("tenantId") String tenantId, @Param("role") RoleUserUpdate roleUpdate);

    /**
     * 用户列表（已授权）
     */
    Page<RoleUserDto> getAuthedUser(@Param("tenantId") String tenantId, @Param("query") RoleUserQuery query, Page<RoleUserDto> page);

    /**
     * 用户列表（未授权）
     */
    Page<RoleUserDto> getUnAuthedUser(@Param("tenantId") String tenantId, @Param("query") RoleUserQuery query, Page<RoleUserDto> page);

    /**
     * 角色权限集
     */
    List<String> getPermitsByRoleCode(String roleCode);
}
