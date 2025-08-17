/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.rabc.HubPost;
import com.cowave.hub.admin.infra.rabc.mapper.HubPostMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubPostDao extends ServiceImpl<HubPostMapper, HubPost> {

    /**
     * 列表查询（岗位id）
     */
    public List<HubPost> listByIds(String tenantId, List<Integer> postIds) {
        return lambdaQuery()
                .eq(HubPost::getTenantId, tenantId)
                .in(HubPost::getPostId, postIds)
                .list();
    }

    /**
     * 岗位名称查询
     */
    public String getNameById(String tenantId, Integer postId){
        return lambdaQuery()
                .eq(HubPost::getTenantId, tenantId)
                .eq(HubPost::getPostId, postId)
                .select(HubPost::getPostName)
                .oneOpt().map(HubPost::getPostName).orElse(null);
    }

    /**
     * 修改岗位信息
     */
    public void updatePost(HubPost hubPost){
        lambdaUpdate()
                .eq(HubPost::getPostId, hubPost.getPostId())
                .set(HubPost::getUpdateBy, Access.userCode())
                .set(HubPost::getUpdateTime, new Date())
                .set(HubPost::getPostCode, hubPost.getPostCode())
                .set(HubPost::getPostType, hubPost.getPostType())
                .set(HubPost::getPostName, hubPost.getPostName())
                .set(HubPost::getPostLevel, hubPost.getPostLevel())
                .set(HubPost::getPostStatus, hubPost.getPostStatus())
                .set(HubPost::getRemark, hubPost.getRemark())
                .update();
    }
}
