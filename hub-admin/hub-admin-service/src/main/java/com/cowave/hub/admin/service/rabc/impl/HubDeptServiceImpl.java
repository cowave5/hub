/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.sys.vo.TreeNode;
import com.cowave.hub.admin.domain.rabc.HubDept;
import com.cowave.hub.admin.domain.rabc.HubDeptPost;
import com.cowave.hub.admin.domain.rabc.HubUserDept;
import com.cowave.hub.admin.domain.rabc.dto.*;
import com.cowave.hub.admin.domain.rabc.request.*;
import com.cowave.hub.admin.infra.rabc.dao.HubDeptDao;
import com.cowave.hub.admin.infra.rabc.dao.HubDeptPostDao;
import com.cowave.hub.admin.infra.rabc.dao.HubDeptDiagramDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDeptDao;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubDeptDtoMapper;
import com.cowave.hub.admin.service.rabc.HubDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubDeptServiceImpl implements HubDeptService {
    private final HubDeptDao hubDeptDao;
    private final HubDeptDiagramDao hubDeptDiagramDao;
    private final HubUserDeptDao hubUserDeptDao;
    private final HubDeptPostDao hubDeptPostDao;
    private final HubDeptDtoMapper hubDeptDtoMapper;

    @Override
    public List<DeptListDto> list(String tenantId, DeptQuery query) {
        return hubDeptDtoMapper.list(tenantId, query);
    }

    @Override
    public DeptInfoDto info(String tenantId, Integer deptId) {
        return hubDeptDtoMapper.info(tenantId, deptId);
    }

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, DeptCreate deptCreate) {
        // 新增部门
        hubDeptDao.save(deptCreate);
        // 上级部门
        hubDeptDiagramDao.saveBatch(deptCreate.getDeptParents());
    }

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void delete(String tenantId, List<Integer> deptIds) {
        HttpAsserts.isTrue(hubDeptDiagramDao.countChildByIds(deptIds) == 0, BAD_REQUEST, "{admin.dept.child.forbid.delete}");

        // 操作日志
        List<HubDept> list = hubDeptDao.listByIds(tenantId, deptIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubDept::getDeptId);
        if (!deleteList.isEmpty()) {
            // 删除部门
            hubDeptDao.removeByIds(deleteList);
            // 删除部门岗位
            hubDeptPostDao.removeByDeptIds(deleteList);
            // 删除用户部门岗位
            hubUserDeptDao.removeByDeptIds(deleteList);
            // 上级部门关系
            hubDeptDiagramDao.deleteParentsByDeptIds(deleteList);
            // 部门用户关系
            hubUserDeptDao.clearByDeptIds(deleteList);
        }
    }

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, DeptCreate deptCreate) {
        Integer deptId = deptCreate.getDeptId();
        HttpAsserts.notNull(deptId, BAD_REQUEST, "{admin.dept.id.null}");

        // 校验&操作日志
        DeptInfoDto preDept = hubDeptDtoMapper.info(tenantId, deptId);
        HttpAsserts.notNull(preDept, NOT_FOUND, "{admin.dept.not.exist}");
        OperationContext.prepareContent(preDept);

        // 更新部门
        hubDeptDao.updateDept(deptCreate);

        // 上级部门
        hubDeptDiagramDao.deleteParentsByDeptId(deptId);
        // 检测循环，children与parents不能有交集
        List<Integer> childIds = hubDeptDtoMapper.childIds(deptId);
        childIds.add(deptId);
        List<Integer> parentIds = deptCreate.getParentIds();
        HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
        hubDeptDiagramDao.saveBatch(deptCreate.getDeptParents());
    }

    @Override
    public List<HubDept> listForExport(String tenantId) {
        return hubDeptDao.list(tenantId);
    }

    @Override
    public List<Tree<Integer>> getDiagram(String tenantId, Integer deptId) {
        Tree<Integer> tree = hubDeptDao.getDeptDiagram(tenantId);
        // 如果没有传deptId，或者传的根部门id，则返回整棵树
        if (deptId == null || deptId.equals(tree.getId())) {
            return List.of(tree);
        }
        // 空树
        if (CollectionUtils.isEmpty(tree.getChildren())) {
            return List.of(new Tree<>());
        }
        // 广度优先，如果节点id与deptId一样则返回
        Deque<Tree<Integer>> queue = new LinkedList<>(tree.getChildren());
        while (!queue.isEmpty()) {
            tree = queue.pop();
            if (Objects.equals(deptId, tree.getId())) {
                return List.of(tree);
            }
            if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                queue.addAll(tree.getChildren());
            }
        }
        return List.of(new Tree<>());
    }

    @Cacheable(value = DEPT_POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> getPostDiagram(String tenantId) {
        // 部门树
        Tree<Integer> tree = hubDeptDao.getDeptDiagram(tenantId);
        Tree<String> deptTree = convertTree(tree);
        // 部门岗位
        List<TreeNode> list = hubDeptDtoMapper.listPostDiagramNode(tenantId);
        Map<String, List<Tree<String>>> deptPostMap = new HashMap<>();
        for (TreeNode option : list) {
            deptPostMap.put(option.getPid(), option.getChildren());
        }

        // tree.id = deptId, label = deptName
        Tree<String> root = deptTree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> postList = deptPostMap.get(root.getId().split("-")[0]);
            List<Tree<String>> children = root.getChildren();
            if (children != null) {
                queue.addAll(root.getChildren());
                if (CollectionUtils.isNotEmpty(postList)) {
                    children.addAll(postList);
                }
            } else {
                if (CollectionUtils.isNotEmpty(postList)) {
                    root.setChildren(postList);
                } else {
                    root.put("isDisabled", true);
                }
            }
        }
        return deptTree;
    }

    @Cacheable(value = DEPT_USER_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> getUserDiagram(String tenantId) {
        // 部门树
        Tree<Integer> tree = hubDeptDao.getDeptDiagram(tenantId);
        Tree<String> deptTree = convertTree(tree);
        // 部门用户
        List<TreeNode> deptUserList = hubDeptDtoMapper.listUserDiagramNode(tenantId);
        Map<String, List<Tree<String>>> deptUserMap = new HashMap<>();
        for (TreeNode deptUser : deptUserList) {
            deptUserMap.put(deptUser.getPid(), deptUser.getChildren());
        }

        Tree<String> root = deptTree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> userList = deptUserMap.get(root.getId().split("-")[0]);
            List<Tree<String>> children = root.getChildren();
            if (children != null) {
                queue.addAll(root.getChildren());
                if (CollectionUtils.isNotEmpty(userList)) {
                    children.addAll(userList);
                }
            } else {
                if (CollectionUtils.isNotEmpty(userList)) {
                    root.setChildren(userList);
                }
            }
        }
        return deptTree;
    }

    private Tree<String> convertTree(Tree<Integer> tree) {
        Tree<String> strTree = new Tree<>();
        strTree.setId(tree.getId() + "-0");
        strTree.put("isDept", true);
        strTree.setParentId(String.valueOf(tree.get("pid")));
        strTree.put("label", String.valueOf(tree.get("label")));
        if (tree.hasChild()) {
            List<Tree<String>> strChildren = tree.getChildren().stream()
                    .map(this::convertTree).collect(Collectors.toList());
            strTree.setChildren(strChildren);
        }
        return strTree;
    }

    @CacheEvict(value = {DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void addPosts(String tenantId, List<HubDeptPost> list) {
        if(list.isEmpty()){
            return;
        }

        // 删除原来挂在dept=0下面的记录
        List<HubDeptPost> insertList = hubDeptDtoMapper.insertDeptPosts(tenantId, list);
        List<Integer> postIds = Collections.copyToList(insertList, HubDeptPost::getPostId);
        hubUserDeptDao.removeByDeptPosts(0, postIds);

        // 默认岗位数校验
        List<Integer> deptIdList = hubDeptDtoMapper.deptWithMultiDefaultPost();
        HttpAsserts.isEmpty(deptIdList, BAD_REQUEST, "{admin.dept.post.default.max}");
    }

    @CacheEvict(value = {DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void removePosts(String tenantId, Integer deptId, List<Integer> postIds) {
        if(CollectionUtils.isEmpty(postIds)) {
            return;
        }
        HubDept hubDept = hubDeptDao.getById(tenantId, deptId);
        HttpAsserts.notNull(hubDept, NOT_FOUND, "{admin.dept.not.exist}");

        hubDeptPostDao.removePostOfDept(deptId, postIds);
        hubUserDeptDao.removeByDeptPosts(deptId, postIds);
    }

    @Override
    public Page<DeptPostDto> getConfiguredPosts(String tenantId, DeptPostQuery query) {
        return hubDeptDtoMapper.getConfiguredPosts(tenantId, query, Access.page());
    }

    @Override
    public Page<DeptPostDto> getUnConfiguredPosts(String tenantId, DeptPostQuery query) {
        return hubDeptDtoMapper.getUnConfiguredPosts(tenantId, query, Access.page());
    }

    @CacheEvict(value = {DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void addMembers(String tenantId, List<HubUserDept> list) {
        if(list.isEmpty()){
            return;
        }
        hubDeptDtoMapper.insertDeptUsers(tenantId, list);
    }

    @CacheEvict(value = {DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void removeMembers(String tenantId, Integer deptId, List<Integer> userIds) {
        HubDept hubDept = hubDeptDao.getById(tenantId, deptId);
        if (hubDept == null) {
            return;
        }
        hubUserDeptDao.removeUserOfDept(deptId, userIds);
    }

    @Override
    public Page<DeptUserDto> getJoinedMembers(String tenantId, DeptUserQuery query) {
        return hubDeptDtoMapper.getJoinedMembers(tenantId, query, Access.page());
    }

    @Override
    public Page<DeptUserDto> getUnJoinedMembers(String tenantId, DeptUserQuery query) {
        return hubDeptDtoMapper.getUnJoinedMembers(tenantId, query, Access.page());
    }

    @Override
    public List<UserNameDto> getCandidatesByCode(String tenantId, String deptCode) {
        return hubDeptDtoMapper.getCandidatesByCode(tenantId, deptCode);
    }

    @Override
    public List<String> getNamesById(String tenantId, List<Integer> deptIds) {
        return hubDeptDao.getNamesById(tenantId, deptIds);
    }
}
