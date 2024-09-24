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
import com.cowave.sys.model.resource.Beam;
import com.cowave.sys.resource.api.mapper.BeamMapper;
import com.cowave.sys.resource.api.service.BeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class BeamServiceImpl implements BeamService {

    private final BeamMapper beamMapper;

    @Override
    public void add(Beam beam) {
        beamMapper.insert(beam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(Beam beam) {
        Asserts.notNull(beam.getBeamId(), "波束id不能为空");
        beamMapper.update(beam);
        if(beam.isSyncResource()){
            beamMapper.updateUpPool(beam);
            beamMapper.updateDownPool(beam);
        }
    }

    @Override
    public void remove(Integer beamId) {
        String tspdName = beamMapper.existTspdName(beamId);
        Asserts.isNull(tspdName, "转发器[" + tspdName + "]正在使用此波束，不允许删除");
        beamMapper.delete(beamId);
    }

    @Override
    public Beam info(Integer beamId) {
        return beamMapper.info(beamId);
    }
}
