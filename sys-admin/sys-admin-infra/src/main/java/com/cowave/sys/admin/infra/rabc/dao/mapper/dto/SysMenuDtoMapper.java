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

import com.cowave.sys.admin.domain.rabc.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Mapper
public interface SysMenuDtoMapper {

    /**
     * 删除当前以及子菜单的角色关联
     */
    void loopDeleteMenuRoles(Integer menuId);

    /**
     * 删除当前以及子菜单
     */
    void loopDeleteMenus(Integer menuId);

    /**
     * 用户权限
     */
    List<String> getPermitsByUserId(Integer userId);

    /**
     * 角色菜单
     */
    List<SysMenu> getMenusByRole(List<String> roleList);

    /**
     * 角色Api菜单
     */
    List<SysMenu> getApiMenusByRole(List<String> roleList);
}
