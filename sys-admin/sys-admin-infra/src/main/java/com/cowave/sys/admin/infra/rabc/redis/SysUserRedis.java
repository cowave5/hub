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
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysUserRedis implements ApplicationRunner {
    private static final String KEY_USER = "sys-admin:tree:user";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final RedisHelper redisHelper;
    private final SysUserDtoMapper sysUserDtoMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of user tree ...");
        refreshDiagram();
    }

    public void refreshDiagram() {
        List<TreeNode> list = sysUserDtoMapper.getTreeNodes();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("content", u.getContent());
        });
        redisHelper.putValue(KEY_USER, deptTree.get(0));
    }

    public Tree<String> getDiagram() {
        return redisHelper.getValue(KEY_USER);
    }
}
