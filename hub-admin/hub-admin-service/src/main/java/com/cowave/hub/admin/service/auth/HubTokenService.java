/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth;

import com.cowave.hub.admin.domain.auth.request.TokenRequest;
import com.cowave.hub.admin.domain.auth.vo.TokenVo;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubTokenService {

    /**
     * 列表
     */
    List<TokenVo> listApiToken();

    /**
     * 创建用户令牌
     */
    String creatApiToken(TokenRequest request);

    /**
     * 删除用户令牌
     */
    void deleteApiToken(Integer tokenId);
}
