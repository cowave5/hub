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
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysPost;
import com.cowave.sys.admin.domain.rabc.SysPostTree;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.sys.admin.domain.rabc.vo.DiagramNode;
import com.cowave.sys.admin.infra.rabc.dao.*;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysPostDtoMapper;
import com.cowave.sys.admin.service.rabc.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.AdminRedisKeys.*;
import static com.cowave.sys.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysPostServiceImpl implements SysPostService {
    private final SysTenantDao sysTenantDao;
    private final SysPostDao sysPostDao;
    private final SysPostTreeDao sysPostTreeDao;
    private final SysUserDeptDao sysUserDeptDao;
    private final SysPostDtoMapper sysPostDtoMapper;
    private final SysDeptPostDao sysDeptPostDao;

    @Override
    public Page<SysPost> pageList(String tenantId, DeptPostQuery query) {
        return sysPostDtoMapper.pageList(tenantId, query, Access.page());
    }

    @Override
    public List<SysPost> list(String tenantId, DeptPostQuery query) {
        return sysPostDtoMapper.list(tenantId, query);
    }

    @Override
    public PostInfoDto info(String tenantId, Integer postId) {
        return sysPostDtoMapper.info(tenantId, postId);
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, PostInfoDto sysPost) {
        // 新增岗位
        sysPostDao.save(sysPost);
        // 上级岗位
        inputPostParents(tenantId, sysPost, false);
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void delete(String tenantId, List<Integer> postIds) {
        long postUserCount = sysUserDeptDao.countUserByPostIds(postIds);
        HttpAsserts.isTrue(postUserCount == 0, BAD_REQUEST, "{admin.post.forbid.user.delete}");

        // 操作日志
        List<SysPost> list = sysPostDao.listByIds(tenantId, postIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, SysPost::getPostId);
        if (!deleteList.isEmpty()) {
            // 删除岗位
            sysPostDao.removeByIds(deleteList);
            // 删除部门岗位
            sysDeptPostDao.removeByPostIds(deleteList);
            // 删除用户部门岗位
            sysUserDeptDao.removeByPostIds(deleteList);
            // 上级岗位关系
            sysPostTreeDao.deleteParentByIds(deleteList);
            // 下级岗位关系
            sysPostTreeDao.deleteChildByIds(deleteList);
        }
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, PostInfoDto sysPost) {
        HttpAsserts.notNull(sysPost.getPostId(), BAD_REQUEST, "{admin.post.id.null}");

        // 操作日志
        PostInfoDto prePost = sysPostDtoMapper.info(tenantId, sysPost.getPostId());
        HttpAsserts.notNull(prePost, NOT_FOUND, "{admin.post.not.exist}", sysPost.getPostId());
        OperationContext.prepareContent(prePost);

        // 更新岗位
        sysPostDao.updatePost(sysPost);
        // 上级岗位
        inputPostParents(tenantId, sysPost, true);
    }

    private void inputPostParents(String tenantId, PostInfoDto sysPost, boolean overwrite){
        if(overwrite){
            sysPostTreeDao.deleteParentById(sysPost.getPostId());
        }
        // 循环校验
        int parentId = sysPost.getParentId();
        if (parentId > 0 && overwrite) {
            List<Integer> childIds = sysPostDtoMapper.childIds(sysPost.getPostId());
            childIds.add(sysPost.getPostId());
            HttpAsserts.isFalse(childIds.contains(parentId), BAD_REQUEST, "{admin.post.tree.cycle}");
        }
        sysPostTreeDao.save(new SysPostTree(sysPost.getPostId(), parentId, tenantId));
    }

    @Cacheable(value = POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> getDiagram(String tenantId) {
        List<DiagramNode> list = sysPostDtoMapper.listDiagramNodes(tenantId);
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

    @Override
    public List<UserNameDto> getCandidatesByCode(String tenantId, String postCode) {
        return sysPostDtoMapper.getCandidatesByCode(tenantId, postCode);
    }

    @Override
    public String getNameById(String tenantId, Integer postId) {
        return sysPostDao.getNameById(tenantId, postId);
    }

    @Override
    public List<String> getNameOfDeptPost(String tenantId, List<String> deptPosts) {
        List<String> list = new ArrayList<>();
        for (String deptPostId : deptPosts) {
            String[] arr = deptPostId.split("-");
            list.add(sysPostDtoMapper.getNameOfDeptPost(tenantId, Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }
        return list;
    }
}
