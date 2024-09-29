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
import com.cowave.sys.admin.domain.rabc.SysPost;
import com.cowave.sys.admin.domain.rabc.SysPostTree;
import com.cowave.sys.admin.domain.rabc.dto.PostInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.UserNameDto;
import com.cowave.sys.admin.domain.rabc.request.DeptPostQuery;
import com.cowave.sys.admin.infra.rabc.dao.SysPostDao;
import com.cowave.sys.admin.infra.rabc.dao.SysPostTreeDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysPostDtoMapper;
import com.cowave.sys.admin.infra.rabc.redis.SysDeptRedis;
import com.cowave.sys.admin.infra.rabc.redis.SysPostRedis;
import com.cowave.sys.admin.service.rabc.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysPostServiceImpl implements SysPostService {
    private final SysDeptRedis sysDeptRedis;
    private final SysPostRedis sysPostRedis;
    private final SysPostDao sysPostDao;
    private final SysPostTreeDao sysPostTreeDao;
    private final SysUserDeptDao sysUserDeptDao;
    private final SysPostDtoMapper sysPostDtoMapper;

    @Override
    public Page<SysPost> pageList(DeptPostQuery query) {
        return sysPostDtoMapper.pageList(Access.page(), query);
    }

    @Override
    public List<SysPost> list(DeptPostQuery query) {
        return sysPostDtoMapper.list(query);
    }

    @Override
    public PostInfoDto info(Integer postId) {
        return sysPostDtoMapper.info(postId);
    }

    @Override
    public void add(PostInfoDto sysPost) {
        // 新增岗位
        sysPostDao.save(sysPost);
        // 上级岗位
        inputPostParents(sysPost, false);
    }

    @Override
    public void delete(List<Integer> postIds) {
        long postUserCount = sysUserDeptDao.countUserByPostIds(postIds);
        HttpAsserts.isTrue(postUserCount == 0, BAD_REQUEST, "{admin.post.forbid.user.delete}");

        // 操作日志
        List<SysPost> list = sysPostDao.listByIds(postIds);
        OperationContext.prepareContent(list);

        // 删除岗位
        sysPostDao.removeByIds(postIds);
        // 上级岗位关系
        sysPostTreeDao.deleteParentByIds(postIds);
        // 下级岗位关系
        sysPostTreeDao.deleteChildByIds(postIds);
    }

    @Override
    public void edit(PostInfoDto sysPost) {
        HttpAsserts.notNull(sysPost.getPostId(), BAD_REQUEST, "{admin.post.id.notnull}");

        // 操作日志
        PostInfoDto prePost = sysPostDtoMapper.info(sysPost.getPostId());
        HttpAsserts.notNull(prePost, NOT_FOUND, "{admin.post.not.exist}", sysPost.getPostId());
        OperationContext.prepareContent(prePost);

        // 更新岗位
        sysPostDao.updatePost(sysPost);
        // 上级岗位
        inputPostParents(sysPost, true);
    }

    private void inputPostParents(PostInfoDto sysPost, boolean overwrite){
        if(overwrite){
            sysPostTreeDao.deleteParentById(sysPost.getPostId());
        }

        Integer parentId = sysPost.getParentId();
        if(parentId != null){
            if(overwrite){
                List<Integer> childIds = sysPostDtoMapper.childIds(sysPost.getPostId());
                childIds.add(sysPost.getPostId());
                HttpAsserts.isFalse(childIds.contains(parentId), BAD_REQUEST, "{admin.post.tree.cycle}");
            }
            sysPostTreeDao.save(new SysPostTree(sysPost.getPostId(), parentId, null));
        }
    }

    @Override
    public Tree<String> getDiagram() {
        return sysPostRedis.getDiagram();
    }

    @Override
    public void refreshDiagram() {
        sysPostRedis.refreshDiagram();
        sysDeptRedis.refreshPostDiagram();
    }

    @Override
    public List<UserNameDto> getCandidatesByCode(String postCode) {
        return sysPostDtoMapper.getCandidatesByCode(postCode);
    }

    @Override
    public String getNameById(Integer postId) {
        return sysPostDao.getNameById(postId);
    }

    @Override
    public List<String> getNameOfDeptPost(List<String> deptPosts) {
        List<String> list = new ArrayList<>();
        for (String deptPostId : deptPosts) {
            String[] arr = deptPostId.split("-");
            list.add(sysPostDtoMapper.getNameOfDeptPost(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }
        return list;
    }
}
