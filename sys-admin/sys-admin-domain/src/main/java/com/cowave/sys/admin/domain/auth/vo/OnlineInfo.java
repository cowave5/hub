/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth.vo;

import com.cowave.commons.framework.access.security.AccessTokenInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OnlineInfo {

    /**
     * Refresh Token id
     */
    private String refreshId;

    /**
     * 令牌类型
     */
    private String authType;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 集群
     */
    private String cluster;

    /**
     * 登录iP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date loginTime;

    /**
     * 访问令牌
     */
    private List<AccessTokenInfo> accessList;
}
