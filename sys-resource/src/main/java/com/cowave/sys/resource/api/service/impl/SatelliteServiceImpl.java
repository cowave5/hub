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
import com.cowave.sys.model.resource.Satellite;
import com.cowave.sys.resource.api.entity.TreeNode;
import com.cowave.sys.resource.api.mapper.SatelliteMapper;
import com.cowave.sys.resource.api.service.SatelliteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SatelliteServiceImpl implements SatelliteService {

    private final SatelliteMapper satelliteMapper;

    @Override
    public void add(Satellite satellite) {
        satelliteMapper.insert(satellite);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(Satellite satellite) {
        Asserts.notNull(satellite.getSatId(), "卫星id不能为空");
        satelliteMapper.update(satellite);
        if(satellite.isSyncResource()){
            satelliteMapper.updatePool(satellite);
        }
    }

    @Override
    public void remove(Integer satId) {
        String tspdName = satelliteMapper.existTspdName(satId);
        Asserts.isNull(tspdName, "转发器[" + tspdName + "]正在使用此卫星，不允许删除");
        satelliteMapper.delete(satId);
    }

    @Override
    public Satellite info(Integer satId) {
        return satelliteMapper.info(satId);
    }

    @Override
    public List<TreeNode> tree() {
        Map<Integer, TreeNode> satelliteBeams = mergeChild(satelliteMapper.treeBeams());
        Map<Integer, TreeNode> beamTunnels = mergeChild(satelliteMapper.treeTunnels());
        List<TreeNode> satellites = satelliteMapper.treeSatellites();
        for(TreeNode satellite : satellites){
            TreeNode satelliteBeam = satelliteBeams.get(satellite.getId());
            if(satelliteBeam != null){
                List<TreeNode> beams = satelliteBeam.getChildren();
                for(TreeNode beam : beams){
                    TreeNode beamTunnel = beamTunnels.get(beam.getId());
                    if(beamTunnel != null){
                        beam.setChildren(beamTunnel.getChildren());
                    }
                }
                satellite.setChildren(beams);
            }
        }
        return satellites;
    }

    private Map<Integer, TreeNode> mergeChild(List<TreeNode> childs){
        Map<Integer, TreeNode> parents = new HashMap<>();
        for(TreeNode child : childs){
            TreeNode parent = parents.get(child.getPid());
            if(parent == null){
                List<TreeNode> children = new ArrayList<>();
                children.add(child.clone());
                child.setChildren(children);
                parents.put(child.getPid(), child);
            }else{
                parent.getChildren().add(child);
            }
        }
        return parents;
    }
}
