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

    public List<SysAttach> queryList(Long masterId, String attachGroup, String attachType){
        return lambdaQuery()
                .eq(masterId != null, SysAttach::getMasterId, masterId)
                .eq(attachGroup != null, SysAttach::getAttachGroup, attachGroup)
                .eq(attachType != null, SysAttach::getAttachType, attachType)
                .list();
    }

    public SysAttach latestOfMaster(Long masterId, String attachGroup){
        return lambdaQuery()
                .eq(SysAttach::getAttachStatus, 1)
                .eq(SysAttach::getMasterId, masterId)
                .eq(SysAttach::getAttachGroup, attachGroup)
                .orderByDesc(SysAttach::getCreateTime)
                .last("LIMIT 1").one();
    }

    public void updateMasterById(Long masterId, Long attachId){
        lambdaUpdate()
                .eq(SysAttach::getAttachId, attachId)
                .set(SysAttach::getMasterId, masterId)
                .set(SysAttach::getAttachStatus, 1)
                .update();
    }
}
