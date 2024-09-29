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

import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import com.cowave.sys.admin.domain.base.vo.TreeNode;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.UserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserListDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.domain.rabc.request.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 *
 * @author shanhuiming
 */
@Mapper
public interface SysUserDtoMapper {

    /**
     * 列表
     */
    List<UserListDto> list(UserQuery query);

    /**
     * 计数
     */
    int count(UserQuery query);

    /**
     * 单位用户列表
     */
    List<UserListDto> listOfDept(UserQuery query);

    /**
     * 单位用户计数
     */
    int countOfDept(UserQuery query);

    /**
     * 详情
     */
    UserInfoDto getById(Integer userId);

    /**
     * 下级用户id列表
     */
    List<Integer> childIds(Integer userId);

    /**
     * 用户权限集
     */
    List<String> getUserPermits(Integer userId);

    /**
     * 导入用户
     */
    void batchInsert(@Param("list") List<SysUser> list, @Param("overwrite") boolean overwrite);

    /**
     * 人员关系
     */
    List<TreeNode> getTreeNodes();

    /**
     * 用户流程候选人
     */
    List<UserNameDto> getUserCandidates(Integer userId);

    /**
     * 用户个人信息
     */
    UserProfile getUserProfile(Integer userId);
}
