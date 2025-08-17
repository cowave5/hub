/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.auth.HubTokenMenu;
import com.cowave.hub.admin.infra.auth.mapper.HubTokenMenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubTokenMenuDao extends ServiceImpl<HubTokenMenuMapper, HubTokenMenu> {

    public void removeByTokenId(Integer tokenId){
        lambdaUpdate().eq(HubTokenMenu::getTokenId, tokenId).remove();
    }

    public List<Integer> listMenusByTokenId(Integer tokenId){
        List<HubTokenMenu> list = lambdaQuery().eq(HubTokenMenu::getTokenId, tokenId).list();
        return Collections.copyToList(list, HubTokenMenu::getMenuId);
    }
}
