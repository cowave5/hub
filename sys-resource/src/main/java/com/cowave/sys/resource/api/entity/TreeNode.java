/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.entity;

import com.cowave.sys.model.resource.ResourcePool;
import lombok.Data;

import java.util.List;

/**
 * 树节点
 *
 * @author shanhuiming
 */
@Data
public class TreeNode implements Cloneable{

    private Integer id;

    private Integer pid;

    private String name;

    private Integer nodeType;

    private List<TreeNode> children;

    @Override
    public TreeNode clone() {
        try {
            return (TreeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    public static TreeNode newTspdPoolNode(ResourcePool pool){
        TreeNode node = new TreeNode();
        node.setNodeType(1);
        node.setId(pool.getTspdId());
        node.setName(pool.getTspdName());
        node.setPid(pool.getDownBeamId());
        return node;
    }

    public static TreeNode newBeamPoolNode(ResourcePool pool){
        TreeNode node = new TreeNode();
        node.setId(pool.getDownBeamId());
        node.setName(pool.getDownBeamName());
        node.setPid(pool.getSatId());
        return node;
    }

    public static TreeNode newSatellitePoolNode(ResourcePool pool){
        TreeNode node = new TreeNode();
        node.setId(pool.getSatId());
        node.setName(pool.getSatName());
        node.setPid(0);
        return node;
    }
}
