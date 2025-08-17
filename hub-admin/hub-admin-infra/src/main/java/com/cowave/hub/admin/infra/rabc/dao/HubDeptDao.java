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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.rabc.HubDept;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.request.DeptCreate;
import com.cowave.hub.admin.domain.rabc.vo.DiagramNode;
import com.cowave.hub.admin.infra.rabc.mapper.HubDeptMapper;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubDeptDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_DIAGRAM;
import static com.cowave.hub.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubDeptDao extends ServiceImpl<HubDeptMapper, HubDept> {

    private final HubDeptDtoMapper hubDeptDtoMapper;

    private final HubTenantDao hubTenantDao;

    @Cacheable(value = DEPT_DIAGRAM, key = "#tenantId")
    public Tree<Integer> getDeptDiagram(String tenantId) {
        List<DiagramNode> list = hubDeptDtoMapper.listDiagramNodes(tenantId);
        // 根节点
        HubTenant hubTenant = hubTenantDao.getById(tenantId);
        list.add(DiagramNode.newRootNode(hubTenant.getTenantName()));
        // 构造Tree
		return TreeUtil.build(list, -1, DIAGRAM_CONFIG, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
        }).get(0);
    }

    /**
     * 部门查询（部门id）
     */
    public HubDept getById(String tenantId, Integer deptId){
        return lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .eq(HubDept::getDeptId, deptId)
                .one();
    }

    /**
     * 列表查询
     */
    public List<HubDept> list(String tenantId){
        return lambdaQuery().eq(HubDept::getTenantId, tenantId).list();
    }

    /**
     * 列表查询（部门id）
     */
    public List<HubDept> listByIds(String tenantId, List<Integer> deptIds){
        return lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .in(HubDept::getDeptId, deptIds)
                .list();
    }

    /**
     * 修改部门信息
     */
    public void updateDept(DeptCreate dept){
        lambdaUpdate()
                .eq(HubDept::getDeptId, dept.getDeptId())
                .set(HubDept::getUpdateBy, Access.userCode())
                .set(HubDept::getUpdateTime, new Date())
                .set(HubDept::getDeptName, dept.getDeptName())
                .set(HubDept::getDeptShort, dept.getDeptShort())
                .set(HubDept::getDeptAddr, dept.getDeptAddr())
                .set(HubDept::getDeptPhone, dept.getDeptPhone())
                .set(HubDept::getRemark, dept.getRemark())
                .update();
    }

    public List<String> getNamesById(String tenantId, List<Integer> deptIds){
        if(deptIds.isEmpty()){
            return List.of();
        }
        List<HubDept> list = lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .in(HubDept::getDeptId, deptIds)
                .select(HubDept::getDeptName)
                .list();
        return Collections.copyToList(list, HubDept::getDeptName);
    }
}
