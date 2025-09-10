/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.rabc.dto;

import com.cowave.hub.admin.domain.rabc.HubUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoDto extends HubUser {

    /**
     * 角色列表
     */
    private List<Long> roleIds;

    /**
     * 上级用户列表
     */
    private List<Long> parentIds;

    /**
     * 部门岗位列表
     */
    private List<String> deptPostIds;
}
