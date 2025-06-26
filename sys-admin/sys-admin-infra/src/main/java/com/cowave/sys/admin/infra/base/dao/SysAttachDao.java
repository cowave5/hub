/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.admin.infra.base.dao.mapper.SysAttachMapper;
import com.cowave.sys.admin.domain.base.SysAttach;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysAttachDao extends ServiceImpl<SysAttachMapper, SysAttach> {

    /**
     * 附件列表
     */
    public List<SysAttach> queryList(String ownerId, String ownerType, String attachType){
        return lambdaQuery()
                .eq(ownerId != null, SysAttach::getOwnerId, ownerId)
                .eq(ownerType != null, SysAttach::getOwnerType, ownerType)
                .eq(attachType != null, SysAttach::getAttachType, attachType)
                .orderByDesc(SysAttach::getCreateTime)
                .list();
    }

    /**
     * 最新的附件
     */
    public SysAttach latestOfOwner(String ownerId, String ownerType, String attachType){
        return lambdaQuery()
                .eq(ownerId != null, SysAttach::getOwnerId, ownerId)
                .eq(ownerType != null, SysAttach::getOwnerType, ownerType)
                .eq(attachType != null, SysAttach::getAttachType, attachType)
                .orderByDesc(SysAttach::getCreateTime)
                .last("LIMIT 1").one();
    }

    /**
     * 更新附件Owner
     */
    public void updateOwnerById(String ownerId, Long attachId){
        lambdaUpdate()
                .eq(SysAttach::getAttachId, attachId)
                .set(SysAttach::getOwnerId, ownerId)
                .update();
    }

    /**
     * 清楚附件Owner
     */
    public void clearWithOwner(String ownerId, String ownerType, String attachType, Long attachId){
        lambdaUpdate()
                .eq(SysAttach::getOwnerId, ownerId)
                .eq(SysAttach::getOwnerType, ownerType)
                .eq(SysAttach::getAttachType, attachType)
                .eq(attachId != null, SysAttach::getAttachId, attachId)
                .set(SysAttach::getOwnerId, null)
                .update();
    }
}
