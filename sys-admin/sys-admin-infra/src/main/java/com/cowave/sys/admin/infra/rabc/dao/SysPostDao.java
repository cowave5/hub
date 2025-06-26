/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.auth.OAuthUser;
import com.cowave.sys.admin.domain.rabc.SysPost;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysPostMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysPostDao extends ServiceImpl<SysPostMapper, SysPost> {

    /**
     * 列表查询（岗位id）
     */
    public List<SysPost> listByIds(String tenantId, List<Integer> postIds) {
        return lambdaQuery()
                .eq(SysPost::getTenantId, tenantId)
                .in(SysPost::getPostId, postIds)
                .list();
    }

    /**
     * 岗位名称查询
     */
    public String getNameById(String tenantId, Integer postId){
        return lambdaQuery()
                .eq(SysPost::getTenantId, tenantId)
                .eq(SysPost::getPostId, postId)
                .select(SysPost::getPostName)
                .oneOpt().map(SysPost::getPostName).orElse(null);
    }

    /**
     * 修改岗位信息
     */
    public void updatePost(SysPost sysPost){
        lambdaUpdate()
                .eq(SysPost::getPostId, sysPost.getPostId())
                .set(SysPost::getUpdateBy, Access.userCode())
                .set(SysPost::getUpdateTime, new Date())
                .set(SysPost::getPostCode, sysPost.getPostCode())
                .set(SysPost::getPostType, sysPost.getPostType())
                .set(SysPost::getPostName, sysPost.getPostName())
                .set(SysPost::getPostLevel, sysPost.getPostLevel())
                .set(SysPost::getPostStatus, sysPost.getPostStatus())
                .set(SysPost::getRemark, sysPost.getRemark())
                .update();
    }
}
