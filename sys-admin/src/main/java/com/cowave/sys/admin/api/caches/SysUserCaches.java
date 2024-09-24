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
import com.cowave.sys.admin.api.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
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
public class SysUserCaches implements ApplicationRunner {

    private static final String KEY_USER = "Tree:user";

    private static final String KEY_DEPT_USER = "Tree:dept-user";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id"). setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    private final RedisHelper redisHelper;

    private final SysUserMapper sysUserMapper;

    @Override
    public void run(ApplicationArguments args) {
        refreshTree();
    }

    public Tree<String> tree() {
        return redisHelper.getValue(KEY_USER);
    }

    public Tree<String> deptUserTree() {
        return redisHelper.getValue(KEY_DEPT_USER);
    }

    public void refreshTree() {
        AccessLogger.info("refresh cache of user tree ...");
        List<TreeNode> list = sysUserMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("content", u.getContent());
        });
        redisHelper.putValue(KEY_USER, deptTree.get(0));
    }

    public void refreshDeptUserTree() {
        AccessLogger.info("refresh cache of dept-user tree ...");
        Tree<String> tree = redisHelper.getValue(SysDeptCaches.KEY_DEPT); // 部门树
        List<TreeChildren> deptUserList = sysUserMapper.deptUserOptions(); // 部门用户
        Map<String, List<Tree<String>>> deptUserMap = new HashMap<>(); // Map<deptId, List<userId>>
        for(TreeChildren deptUser : deptUserList){
            deptUserMap.put(deptUser.getPid(), deptUser.getChildren());
        }

        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            List<Tree<String>> users = deptUserMap.get(root.getId());
            if(children != null) {
                queue.addAll(root.getChildren());
                if(users != null) {
                    children.addAll(users);
                }
            }else{
                root.setChildren(users);
            }
            root.put("isDept", true);
            root.setId(root.getId() + "d"); // 避免dept与user的id相同导致选择问题
        }
        redisHelper.putValue(KEY_DEPT_USER, tree);
    }
}
