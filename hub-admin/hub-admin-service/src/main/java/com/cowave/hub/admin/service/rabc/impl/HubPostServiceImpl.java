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
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.infra.rabc.dao.*;
import com.cowave.hub.admin.domain.rabc.HubPost;
import com.cowave.hub.admin.domain.rabc.HubPostDiagram;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.hub.admin.domain.rabc.dto.UserNameDto;
import com.cowave.hub.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.hub.admin.domain.rabc.vo.DiagramNode;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubPostDtoMapper;
import com.cowave.hub.admin.service.rabc.HubPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.hub.admin.domain.rabc.vo.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubPostServiceImpl implements HubPostService {
    private final HubTenantDao hubTenantDao;
    private final HubPostDao hubPostDao;
    private final HubPostDiagramDao hubPostDiagramDao;
    private final HubUserDeptDao hubUserDeptDao;
    private final HubPostDtoMapper hubPostDtoMapper;
    private final HubDeptPostDao hubDeptPostDao;

    @Override
    public Page<HubPost> pageList(String tenantId, DeptPostQuery query) {
        return hubPostDtoMapper.pageList(tenantId, query, Access.page());
    }

    @Override
    public List<HubPost> list(String tenantId, DeptPostQuery query) {
        return hubPostDtoMapper.list(tenantId, query);
    }

    @Override
    public PostInfoDto info(String tenantId, Integer postId) {
        return hubPostDtoMapper.info(tenantId, postId);
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, PostInfoDto post) {
        // 新增岗位
        hubPostDao.save(post);
        // 上级岗位
        inputPostParents(tenantId, post, false);
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void delete(String tenantId, List<Integer> postIds) {
        long postUserCount = hubUserDeptDao.countUserByPostIds(postIds);
        HttpAsserts.isTrue(postUserCount == 0, BAD_REQUEST, "{admin.post.forbid.user.delete}");

        // 操作日志
        List<HubPost> list = hubPostDao.listByIds(tenantId, postIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubPost::getPostId);
        if (!deleteList.isEmpty()) {
            // 删除岗位
            hubPostDao.removeByIds(deleteList);
            // 删除部门岗位
            hubDeptPostDao.removeByPostIds(deleteList);
            // 删除用户部门岗位
            hubUserDeptDao.removeByPostIds(deleteList);
            // 上级岗位关系
            hubPostDiagramDao.deleteParentByIds(deleteList);
            // 下级岗位关系
            hubPostDiagramDao.deleteChildByIds(deleteList);
        }
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, PostInfoDto post) {
        HttpAsserts.notNull(post.getPostId(), BAD_REQUEST, "{admin.post.id.null}");

        // 操作日志
        PostInfoDto prePost = hubPostDtoMapper.info(tenantId, post.getPostId());
        HttpAsserts.notNull(prePost, NOT_FOUND, "{admin.post.not.exist}", post.getPostId());
        OperationContext.prepareContent(prePost);

        // 更新岗位
        hubPostDao.updatePost(post);
        // 上级岗位
        inputPostParents(tenantId, post, true);
    }

    private void inputPostParents(String tenantId, PostInfoDto post, boolean overwrite){
        if(overwrite){
            hubPostDiagramDao.deleteParentById(post.getPostId());
        }
        // 循环校验
        int parentId = post.getParentId();
        if (parentId > 0 && overwrite) {
            List<Integer> childIds = hubPostDtoMapper.childIds(post.getPostId());
            childIds.add(post.getPostId());
            HttpAsserts.isFalse(childIds.contains(parentId), BAD_REQUEST, "{admin.post.tree.cycle}");
        }
        hubPostDiagramDao.save(new HubPostDiagram(post.getPostId(), parentId, tenantId));
    }

    @Cacheable(value = POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> getDiagram(String tenantId) {
        List<DiagramNode> list = hubPostDtoMapper.listDiagramNodes(tenantId);
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

    @Override
    public List<UserNameDto> getCandidatesByCode(String tenantId, String postCode) {
        return hubPostDtoMapper.getCandidatesByCode(tenantId, postCode);
    }

    @Override
    public String getNameById(String tenantId, Integer postId) {
        return hubPostDao.getNameById(tenantId, postId);
    }

    @Override
    public List<String> getNameOfDeptPost(String tenantId, List<String> deptPosts) {
        List<String> list = new ArrayList<>();
        for (String deptPostId : deptPosts) {
            String[] arr = deptPostId.split("-");
            list.add(hubPostDtoMapper.getNameOfDeptPost(tenantId, Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }
        return list;
    }
}
