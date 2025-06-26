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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.request.DeptCreate;
import com.cowave.sys.admin.domain.rabc.vo.DiagramNode;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysDeptMapper;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.sys.admin.domain.AdminRedisKeys.DEPT_DIAGRAM;
import static com.cowave.sys.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysDeptDao extends ServiceImpl<SysDeptMapper, SysDept> {

    private final SysDeptDtoMapper sysDeptDtoMapper;

    private final SysTenantDao sysTenantDao;

    @Cacheable(value = DEPT_DIAGRAM, key = "#tenantId")
    public Tree<Integer> getDeptDiagram(String tenantId) {
        List<DiagramNode> list = sysDeptDtoMapper.listDiagramNodes(tenantId);
        // 根节点
        SysTenant sysTenant = sysTenantDao.getById(tenantId);
        list.add(DiagramNode.newRootNode(sysTenant.getTenantName()));
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
    public SysDept getById(String tenantId, Integer deptId){
        return lambdaQuery()
                .eq(SysDept::getTenantId, tenantId)
                .eq(SysDept::getDeptId, deptId)
                .one();
    }

    /**
     * 列表查询
     */
    public List<SysDept> list(String tenantId){
        return lambdaQuery().eq(SysDept::getTenantId, tenantId).list();
    }

    /**
     * 列表查询（部门id）
     */
    public List<SysDept> listByIds(String tenantId, List<Integer> deptIds){
        return lambdaQuery()
                .eq(SysDept::getTenantId, tenantId)
                .in(SysDept::getDeptId, deptIds)
                .list();
    }

    /**
     * 修改部门信息
     */
    public void updateDept(DeptCreate dept){
        lambdaUpdate()
                .eq(SysDept::getDeptId, dept.getDeptId())
                .set(SysDept::getUpdateBy, Access.userCode())
                .set(SysDept::getUpdateTime, new Date())
                .set(SysDept::getDeptName, dept.getDeptName())
                .set(SysDept::getDeptShort, dept.getDeptShort())
                .set(SysDept::getDeptAddr, dept.getDeptAddr())
                .set(SysDept::getDeptPhone, dept.getDeptPhone())
                .set(SysDept::getRemark, dept.getRemark())
                .update();
    }

    public List<String> getNamesById(String tenantId, List<Integer> deptIds){
        if(deptIds.isEmpty()){
            return List.of();
        }
        List<SysDept> list = lambdaQuery()
                .eq(SysDept::getTenantId, tenantId)
                .in(SysDept::getDeptId, deptIds)
                .select(SysDept::getDeptName)
                .list();
        return Collections.copyToList(list, SysDept::getDeptName);
    }
}
