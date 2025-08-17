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
import com.cowave.hub.admin.infra.rabc.mapper.HubDeptDiagramMapper;
import com.cowave.hub.admin.domain.rabc.HubDeptDiagram;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubDeptDiagramDao extends ServiceImpl<HubDeptDiagramMapper, HubDeptDiagram> {

    /**
     * 删除上级部门
     */
    public void deleteParentsByDeptId(Integer deptId){
        lambdaUpdate().eq(HubDeptDiagram::getDeptId, deptId).remove();
    }

    /**
     * 删除上级部门
     */
    public void deleteParentsByDeptIds(List<Integer> deptIds){
        lambdaUpdate().in(HubDeptDiagram::getDeptId, deptIds).remove();
    }

    /**
     * 计数：deptIds的下级部门数
     */
    public long countChildByIds(List<Integer> deptIds){
        return lambdaQuery().in(HubDeptDiagram::getParentId, deptIds).count();
    }
}
