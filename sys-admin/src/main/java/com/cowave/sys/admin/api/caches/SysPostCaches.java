/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.caches;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.access.AccessLogger;
import com.cowave.commons.framework.support.redis.RedisHelper;
import com.cowave.sys.admin.api.entity.TreeChildren;
import com.cowave.sys.admin.api.entity.TreeNode;
import com.cowave.sys.admin.api.mapper.SysPostMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysPostCaches implements ApplicationRunner {

    private static final String KEY_POST = "Tree:post";

    private static final String KEY_DEPT_POST = "Tree:dept-post";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id"). setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    private final RedisHelper redisHelper;

    private final SysPostMapper sysPostMapper;

    @Override
    public void run(ApplicationArguments args) {
        refreshTree();
    }

    public Tree<String> tree() {
        return redisHelper.getValue(KEY_POST);
    }

    public Tree<String> deptPostTree() {
        return redisHelper.getValue(KEY_DEPT_POST);
    }

    // id = postId
    public void refreshTree() {
        AccessLogger.info("refresh cache of post tree ...");
        List<TreeNode> list = sysPostMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (p, node) -> {
            node.setId(p.getId());
            node.setParentId(p.getPid());
            node.setName(p.getLabel());
        });
        redisHelper.putValue(KEY_POST, deptTree.get(0));
    }

    // id = deptId-postId, label = postName
    public void refreshDeptPostTree(){
        AccessLogger.info("refresh cache of dept-post tree ...");
        List<TreeChildren> list = sysPostMapper.deptPostOptions();
        Map<String, List<Tree<String>>> map = new HashMap<>();
        for(TreeChildren option : list){
            map.put(option.getPid(), option.getChildren());
        }

        // tree.id = deptId, label = deptName
        Tree<String> tree = redisHelper.getValue(SysDeptCaches.KEY_DEPT);
        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            if(children != null) {
                queue.addAll(root.getChildren());
                children.addAll(map.get(root.getId()));
            }else{
                List<Tree<String>> childs = map.get(root.getId());
                if(CollectionUtils.isEmpty(childs)){
                    root.setChildren(null);
                }else{
                    root.setChildren(map.get(root.getId()));
                }
            }
        }
        redisHelper.putValue(KEY_DEPT_POST, tree);
    }
}
