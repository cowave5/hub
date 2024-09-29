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
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.request.DeptCreate;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysDeptMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysDeptDao extends ServiceImpl<SysDeptMapper, SysDept> {

    /**
     * 修改部门信息
     */
    public void updateDept(DeptCreate dept){
        lambdaUpdate().eq(SysDept::getDeptId, dept.getDeptId())
                .set(SysDept::getUpdateBy, Access.userCode())
                .set(SysDept::getUpdateTime, new Date())
                .set(SysDept::getDeptName, dept.getDeptName())
                .set(SysDept::getDeptShort, dept.getDeptShort())
                .set(SysDept::getDeptAddr, dept.getDeptAddr())
                .set(SysDept::getDeptPhone, dept.getDeptPhone())
                .set(SysDept::getRemark, dept.getRemark())
                .update();
    }

    public List<String> getNamesById(List<Integer> deptIds){
        if(deptIds.isEmpty()){
            return List.of();
        }
        List<SysDept> list = lambdaQuery()
                .in(SysDept::getDeptId, deptIds)
                .select(SysDept::getDeptName)
                .list();
        return Collections.copyToList(list, SysDept::getDeptName);
    }
}
