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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rabc.HubRole;
import com.cowave.hub.admin.domain.rabc.dto.RoleInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.RoleUserDto;
import com.cowave.hub.admin.domain.rabc.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubRoleService {

    /**
     * 列表
     */
    Page<HubRole> list(String tenantId, RoleQuery query);

    /**
     * 详情
     */
    RoleInfoDto info(String tenantId, Integer roleId);

    /**
     * 新增
     */
    void add(String tenantId, HubRole hubRole);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> roleIds);

    /**
     * 修改
     */
    void edit(String tenantId, HubRole hubRole);

    /**
     * 修改角色菜单
     */
    void updateMenus(String tenantId, RoleMenuUpdate roleUpdate);

    /**
     * 授权用户
     */
    void grantUser(String tenantId, RoleUserUpdate roleUpdate);

    /**
     * 取消用户
     */
    void cancelUser(String tenantId, RoleUserUpdate roleUpdate);

    /**
     * 用户列表（已授权）
     */
    Page<RoleUserDto> getAuthedUser(String tenantId, RoleUserQuery query);

    /**
     * 用户列表（未授权）
     */
    Page<RoleUserDto> getUnAuthedUser(String tenantId, RoleUserQuery query);

    /**
     * 角色名称查询
     */
    List<String> getNames(String tenantId, List<Integer> userIds);
}
