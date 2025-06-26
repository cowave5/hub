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
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysDeptPostMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysDeptPostDao extends ServiceImpl<SysDeptPostMapper, SysDeptPost> {

    /**
     * 从部门中移除岗位
     */
    public void removePostOfDept(Integer deptId, List<Integer> postIds){
        lambdaUpdate()
                .eq(SysDeptPost::getDeptId, deptId)
                .in(SysDeptPost::getPostId, postIds)
                .remove();
    }

    /**
     * 删除岗位-关联删除
     */
    public void removeByPostIds(List<Integer> postIds) {
        lambdaUpdate().in(SysDeptPost::getPostId, postIds).remove();
    }

    /**
     * 删除部门-关联删除
     */
    public void removeByDeptIds(List<Integer> deptIds) {
        lambdaUpdate().in(SysDeptPost::getDeptId, deptIds).remove();
    }
}
