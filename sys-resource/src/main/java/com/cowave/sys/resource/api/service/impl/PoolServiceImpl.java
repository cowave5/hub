/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service.impl;

import com.cowave.commons.tools.Asserts;
import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.resource.api.entity.TreeNode;
import com.cowave.sys.resource.api.mapper.PoolMapper;
import com.cowave.sys.resource.api.service.PoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class PoolServiceImpl implements PoolService {

    private final PoolMapper poolMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(ResourcePool pool) {
        pool.validGenerate();
        poolMapper.createByTspd(pool);
        poolMapper.fillSatellite(pool.getId());
        poolMapper.fillUpBeam(pool.getId());
        poolMapper.fillDownBeam(pool.getId());
        poolMapper.fillUpTunnel(pool.getId());
        poolMapper.fillDownTunnel(pool.getId());
    }

    @Override
    public void remove(Long srcId) {
        Asserts.equals(0, poolMapper.countPoolUsages(srcId), "资源存在使用记录，不允许删除");
        poolMapper.delete(srcId);
    }

    @Override
    public Collection<TreeNode> tree() {
        List<ResourcePool> pools = poolMapper.treePools();
        Map<Integer, TreeNode> satelliteNodes = new HashMap<>();
        Map<Integer, TreeNode> beamNodes = new HashMap<>();
        for(ResourcePool pool : pools){
            boolean isNewBeam = false;
            TreeNode beamNode = beamNodes.get(pool.getDownBeamId());
            if(beamNode == null){
                isNewBeam = true;
                beamNode = TreeNode.newBeamPoolNode(pool);
                List<TreeNode> beamChildren = new ArrayList<>();
                beamChildren.add(TreeNode.newTspdPoolNode(pool));
                beamNode.setChildren(beamChildren);
                beamNodes.put(pool.getDownBeamId(), beamNode);
            }else{
                beamNode.getChildren().add(TreeNode.newTspdPoolNode(pool));
            }

            TreeNode satelliteNode = satelliteNodes.get(pool.getSatId());
            if(satelliteNode == null){
                satelliteNode = TreeNode.newSatellitePoolNode(pool);
                List<TreeNode> satelliteChildren = new ArrayList<>();
                satelliteChildren.add(beamNode);
                satelliteNode.setChildren(satelliteChildren);
                satelliteNodes.put(pool.getSatId(), satelliteNode);
            }else if(isNewBeam){
                satelliteNode.getChildren().add(beamNode);
            }
        }
        return satelliteNodes.values();
    }
}
