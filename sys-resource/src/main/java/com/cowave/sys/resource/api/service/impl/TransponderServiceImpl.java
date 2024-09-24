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
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.model.resource.Transponder;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.mapper.PoolMapper;
import com.cowave.sys.resource.api.mapper.TransponderMapper;
import com.cowave.sys.resource.api.mapper.UsageMapper;
import com.cowave.sys.resource.api.service.TransponderService;
import com.cowave.sys.resource.doc.TspdType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class TransponderServiceImpl implements TransponderService {

    private final TransponderMapper tspdMapper;

    private final PoolMapper poolMapper;

    private final UsageMapper usageMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Transponder transponder) {
        validTransponder(transponder);
        tspdMapper.insert(transponder);
        if(TspdType.TRANSPARENT.getCode() == transponder.getTspdType()){
            tspdMapper.updateFreqDiff(transponder.getTspdId(), transponder.getUpTunnelId(), transponder.getDownTunnelId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(Transponder transponder) {
        Asserts.notNull(transponder.getTspdId(), "转发器id不能为空");
        validTransponder(transponder);
        tspdMapper.update(transponder);
        if(TspdType.TRANSPARENT.getCode() == transponder.getTspdType()){
            tspdMapper.updateFreqDiff(transponder.getTspdId(), transponder.getUpTunnelId(), transponder.getDownTunnelId());
        }
        if(transponder.isSyncResource()){
            tspdMapper.updatePool(transponder);
        }
    }

    @Override
    public void remove(Integer tspdId) {
        Asserts.isNull(tspdMapper.existInPool(tspdId), "转发器已存在于资源池，不允许删除");
        tspdMapper.delete(tspdId);
    }

    @Override
    public Transponder info(Integer tspdId) {
        return tspdMapper.info(tspdId);
    }

    @Override
    public List<Transponder> satelliteQuery(Integer satId) {
        return tspdMapper.satelliteQuery(satId);
    }

    @Override
    public List<Transponder> beamQuery(Integer beamId) {
        return tspdMapper.beamQuery(beamId);
    }

    @Override
    public List<Transponder> tunnelQuery(Integer tunnelId) {
        return tspdMapper.tunnelQuery(tunnelId);
    }

    private void validTransponder(Transponder tspd){
        Asserts.notNull(tspdMapper.ensureSatellite(tspd.getSatId()), "卫星不存在");
        Asserts.notNull(tspdMapper.ensureBeam(tspd.getUpBeamId()), "上行波束不存在");
        Asserts.notNull(tspdMapper.ensureBeam(tspd.getDownBeamId()), "下行波束不存在");
        Asserts.notNull(tspdMapper.ensureTunnel(tspd.getUpTunnelId()), "上行通道不存在");
        Asserts.notNull(tspdMapper.ensureTunnel(tspd.getDownTunnelId()), "下行通道不存在");
    }

    @Override
    public List<ResourcePool> poolList(Integer tspdId) {
        return poolMapper.queryByTspdId(tspdId);
    }

    @Override
    public List<ResourceUsage> usageList(Integer tspdId) {
        return usageMapper.queryByTspdId(tspdId);
    }

    @Override
    public List<ResourceUsage> timeUsageList(Usage usage) {
        return usageMapper.queryByTspdIdWithTime(usage);
    }
}
