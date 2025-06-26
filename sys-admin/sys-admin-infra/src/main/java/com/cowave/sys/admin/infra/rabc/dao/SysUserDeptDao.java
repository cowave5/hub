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
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysUserDeptMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserDeptDao extends ServiceImpl<SysUserDeptMapper, SysUserDept> {

    /**
     * 删除用户部门（用户id）
     */
    public void removeByUserId(Integer userId){
        lambdaUpdate().eq(SysUserDept::getUserId, userId).remove();
    }

    /**
     * 统计岗位用户数
     */
    public long countUserByPostIds(List<Integer> postIds){
        return lambdaQuery().in(SysUserDept::getPostId, postIds).count();
    }

    /**
     * 清除部门用户
     */
    public void clearByDeptIds(List<Integer> deptIds){
        lambdaUpdate().in(SysUserDept::getDeptId, deptIds).remove();
    }

    /**
     * 从部门中移除用户
     */
    public void removeUserOfDept(Integer deptId, List<Integer> userIds){
        lambdaUpdate().eq(SysUserDept::getDeptId, deptId).in(SysUserDept::getUserId, userIds).remove();
    }

    /**
     * 删除部门岗位-关联删除
     */
    public void removeByDeptPosts(Integer deptId, List<Integer> postIds) {
        lambdaUpdate().eq(SysUserDept::getDeptId, deptId).in(SysUserDept::getPostId, postIds).remove();
    }

    /**
     * 删除岗位-关联删除
     */
    public void removeByPostIds(List<Integer> postIds) {
        lambdaUpdate().in(SysUserDept::getPostId, postIds).remove();
    }

    /**
     * 删除部门-关联删除
     */
    public void removeByDeptIds(List<Integer> deptIds) {
        lambdaUpdate().in(SysUserDept::getDeptId, deptIds).remove();
    }
}
