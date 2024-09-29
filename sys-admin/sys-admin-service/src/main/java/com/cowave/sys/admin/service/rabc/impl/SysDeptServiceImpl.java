/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.dto.*;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptPostDao;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptTreeDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.infra.rabc.redis.SysDeptRedis;
import com.cowave.sys.admin.service.rabc.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
    private final SysDeptRedis sysDeptRedis;
    private final SysDeptDao sysDeptDao;
    private final SysUserDeptDao sysUserDeptDao;
    private final SysDeptTreeDao sysDeptTreeDao;
    private final SysDeptPostDao sysDeptPostDao;
    private final SysDeptDtoMapper sysDeptDtoMapper;

    @Override
    public List<DeptListDto> list(DeptQuery query) {
        return sysDeptDtoMapper.list(query);
    }

    @Override
    public DeptInfoDto info(Integer deptId) {
        return sysDeptDtoMapper.info(deptId);
    }

    @Override
    public void create(DeptCreate deptCreate) {
        // 新增部门
        sysDeptDao.save(deptCreate);
        // 上级部门
        sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
    }

    @Override
    public void delete(List<Integer> deptIds) {
        HttpAsserts.isTrue(sysDeptTreeDao.countChildByIds(deptIds) == 0, BAD_REQUEST, "{admin.dept.child.forbid.delete}");
        // 操作日志
        List<SysDept> list = sysDeptDao.listByIds(deptIds);
        OperationContext.prepareContent(list);
        // 删除部门
        sysDeptDao.removeByIds(deptIds);
        // 上级部门关系
        sysDeptTreeDao.deleteParentsByDeptIds(deptIds);
        // 部门用户关系
        sysUserDeptDao.clearByDeptIds(deptIds);
    }

    @Override
    public void edit(DeptCreate deptCreate) {
        Integer deptId = deptCreate.getDeptId();
        HttpAsserts.notNull(deptId, BAD_REQUEST, "{admin.dept.id.notnull}");

        // 校验&操作日志
        DeptInfoDto preDept = sysDeptDtoMapper.info(deptId);
        HttpAsserts.notNull(preDept, NOT_FOUND, "{admin.dept.not.exist}");
        OperationContext.prepareContent(preDept);

        // 更新部门
        sysDeptDao.updateDept(deptCreate);

        // 上级部门
        sysDeptTreeDao.deleteParentsByDeptId(deptId);
        List<Integer> parentIds = deptCreate.getParentIds();
        if (CollectionUtils.isNotEmpty(parentIds)) {
            // 检测循环，children与parents不能有交集
            List<Integer> childIds = sysDeptDtoMapper.childIds(deptId);
            childIds.add(deptId);
            HttpAsserts.isTrue(Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
            sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
        }
    }

    @Override
    public List<SysDept> listForExport() {
        return sysDeptDao.list();
    }

    @Override
    public List<Tree<String>> getDiagram(String deptId) {
        return sysDeptRedis.getDiagram(deptId);
    }

    @Override
    public void refreshDiagram() {
        sysDeptRedis.refreshDiagram();
        sysDeptRedis.refreshUserDiagram();
        sysDeptRedis.refreshPostDiagram();
    }

    @Override
    public Tree<String> getPostDiagram() {
        return sysDeptRedis.getPostDiagram();
    }

    @Override
    public Tree<String> getUserDiagram() {
        return sysDeptRedis.getUserDiagram();
    }

    @Override
    public void addPosts(List<SysDeptPost> list) {
        if(list.isEmpty()){
            return;
        }
        sysDeptDtoMapper.insertDeptPosts(list);
        List<Integer> deptIdList = sysDeptDtoMapper.deptWithMultiDefaultPost();
        HttpAsserts.isEmpty(deptIdList, BAD_REQUEST, "{admin.dept.post.default.max}");
    }

    @Override
    public void removePosts(Integer deptId, List<Integer> postIds) {
        sysDeptPostDao.removePostOfDept(deptId, postIds);
    }

    @Override
    public Page<DeptPostDto> getConfiguredPosts(DeptPostQuery query) {
        return sysDeptDtoMapper.getConfiguredPosts(Access.page(), query);
    }

    @Override
    public Page<DeptPostDto> getUnConfiguredPosts(DeptPostQuery query) {
        return sysDeptDtoMapper.getUnConfiguredPosts(Access.page(), query);
    }

    @Override
    public void addMembers(List<SysUserDept> list) {
        sysDeptDtoMapper.insertDeptUsers(list);
    }

    @Override
    public void removeMembers(Integer deptId, List<Integer> userIds) {
         sysUserDeptDao.removeUserOfDept(deptId, userIds);
    }

    @Override
    public Page<DeptUserDto> getJoinedMembers(DeptUserQuery query) {
        return sysDeptDtoMapper.getJoinedMembers(Access.page(), query);
    }

    @Override
    public Page<DeptUserDto> getUnJoinedMembers(DeptUserQuery query) {
        return sysDeptDtoMapper.getUnJoinedMembers(Access.page(), query);
    }

    @Override
    public List<UserNameDto> getCandidatesByCode(String deptCode) {
        return sysDeptDtoMapper.getCandidatesByCode(deptCode);
    }

    @Override
    public List<String> getNamesById(List<Integer> deptIds) {
        return sysDeptDao.getNamesById(deptIds);
    }
}
