/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.redis;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.base.vo.TreeNode;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysPostDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.cowave.sys.admin.domain.AdminRedisKeys.POST_TREE;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysPostRedis implements ApplicationRunner {
    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final RedisHelper redisHelper;
    private final SysPostDtoMapper sysPostDtoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of post tree ...");
        refreshDiagram();
    }

    // id = postId
    public void refreshDiagram() {
        List<TreeNode> list = sysPostDtoMapper.getTreeNodes();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (p, node) -> {
            node.setId(p.getId());
            node.setParentId(p.getPid());
            node.setName(p.getLabel());
        });
        redisHelper.putValue(POST_TREE, deptTree.get(0));
    }

    public Tree<String> getDiagram() {
        return redisHelper.getValue(POST_TREE);
    }
}
