/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.resource.api.entity.TreeNode;

import java.util.Collection;

/**
 *
 * @author shanhuiming
 *
 */
public interface PoolService {

    /**
     * 创建
     */
    void create(ResourcePool pool);

    /**
     * 删除
     */
    void remove(Long srcId);

    /**
     * 树
     */
    Collection<TreeNode> tree();


}
