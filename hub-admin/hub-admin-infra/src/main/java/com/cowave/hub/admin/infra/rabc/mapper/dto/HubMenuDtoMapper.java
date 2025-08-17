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

import com.cowave.hub.admin.domain.rabc.HubMenu;
import com.cowave.hub.admin.domain.rabc.dto.HubMenuTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Mapper
public interface HubMenuDtoMapper {

    /**
     * 操作权限（用户id）
     */
    List<String> listPermitsByUserId(@Param("tenantId") String tenantId, @Param("userId") Integer userId);

    /**
     * 菜单权限（指定角色）
     */
    List<HubMenu> listMenusByRoles(@Param("tenantId") String tenantId, @Param("list") List<String> roleList);

    /**
     * Api令牌权限(管理员)
     */
    List<HubMenuTree>listApiPermitsByAdmin(String tenantId);

    /**
     * Api令牌权限
     */
    List<HubMenuTree> listApiPermitsByRoles(@Param("tenantId") String tenantId, @Param("list") List<String> list);

    /**
     * 删除当前以及子菜单的角色关联
     */
    void loopDeleteMenuRoles(Integer menuId);

    /**
     * 删除当前以及子菜单
     */
    void loopDeleteMenus(Integer menuId);

    /**
     * 菜单树（包含数据权限）
     */
    List<HubMenuTree> listTree(String tenantId);
}
