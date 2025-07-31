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
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserListDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.domain.rabc.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserService {

    /**
     * 列表
     */
    Response.Page<UserListDto> list(String tenantId, UserQuery query);

    /**
     * 详情
     */
    UserInfoDto info(String tenantId, Integer userId);

    /**
     * 新增
     */
    void create(String tenantId, UserCreate user);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> userIds);

    /**
     * 修改
     */
    void edit(String tenantId, UserCreate user);

    /**
     * 修改角色
     */
    void changeRoles(String tenantId, UserRoleUpdate user);

    /**
     * 修改状态
     */
    void changeStatus(String tenantId, UserStatusUpdate user);

    /**
     * 修改密码
     */
    void changePasswd(String tenantId, UserPasswdUpdate user);

    /**
     * 导入用户
     */
    void importUsers(String tenantId, List<SysUser> list, boolean overwrite);

    /**
     * 导出用户
     */
    List<SysUser> listForExport(String tenantId, UserExportQuery userExport);

    /**
     * 用户组织架构
     */
    Tree<Integer> getDiagram(String tenantId);

    /**
     * 用户流程候选人
     */
    List<UserNameDto> getUserCandidates(String tenantId, Integer userId);

    /**
     * 用户名称查询
     */
    List<String> getNamesById(String tenantId, List<Integer> userIds);

    /**
     * 成员列表选项
     */
    Page<SysUser> getUserOptions(String tenantId, UserMemberQuery query);
}
