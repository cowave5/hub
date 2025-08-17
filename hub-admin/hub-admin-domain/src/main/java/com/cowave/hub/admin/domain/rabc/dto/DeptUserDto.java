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

import com.cowave.hub.admin.domain.rabc.enums.YesNo;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class DeptUserDto {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
	 * 用户电话
	 */
	private String userPhone;

    /**
	 * 用户职级
	 */
	private String userRank;

    /**
     * 用户岗位
     */
    private String postName;

    /**
     * 用户默认岗位
     */
    private YesNo isDefault;

    /**
     * 部门负责人
     */
    private YesNo isLeader;
}
