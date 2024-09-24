/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.Satellite;
import com.cowave.sys.resource.api.entity.TreeNode;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface SatelliteService {

    /**
     * 新增
     */
    void add(Satellite satellite);

    /**
     * 修改
     */
    void edit(Satellite satellite);

    /**
     * 删除
     */
    void remove(Integer satId);

    /**
     * 详情
     */
    Satellite info(Integer satId);

    /**
     * 树
     */
    List<TreeNode> tree();
}
