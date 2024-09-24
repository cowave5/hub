/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.sys.model.resource.Satellite;
import com.cowave.sys.resource.api.entity.TreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SatelliteMapper {

    /**
     * 新增
     */
    void insert(Satellite satellite);

    /**
     * 修改
     */
    void update(Satellite satellite);

    /**
     * 同步修改资源
     */
    void updatePool(Satellite satellite);

    /**
     * 删除
     */
    void delete(Integer satId);

    /**
     * 使用此卫星的转发器名称
     */
    String existTspdName(Integer satId);

    /**
     * 卫星树
     */
    List<TreeNode> treeSatellites();

    /**
     * 波束树
     */
    List<TreeNode> treeBeams();

    /**
     * 通道树
     */
    List<TreeNode> treeTunnels();

    /**
     * 详情
     */
    Satellite info(Integer satId);
}
