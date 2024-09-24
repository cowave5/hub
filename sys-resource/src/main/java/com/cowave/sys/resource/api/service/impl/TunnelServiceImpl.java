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
import com.cowave.sys.model.resource.Tunnel;
import com.cowave.sys.resource.api.mapper.TunnelMapper;
import com.cowave.sys.resource.api.service.TunnelService;
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
public class TunnelServiceImpl implements TunnelService {

    private final TunnelMapper tunnelMapper;

    @Override
    public void add(Tunnel tunnel) {
        tunnelMapper.insert(tunnel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(Tunnel tunnel) {
        Asserts.notNull(tunnel.getTunnelId(), "通道id不能为空");
        tunnelMapper.update(tunnel);
        if(tunnel.isSyncResource()){
            tunnelMapper.updateUpPool(tunnel);
            tunnelMapper.updateDownPool(tunnel);
        }
    }

    @Override
    public void remove(Integer tunnelId) {
        String tspdName = tunnelMapper.existTspdName(tunnelId);
        Asserts.isNull(tspdName, "转发器[" + tspdName + "]正在使用此通过，不允许删除");
        tunnelMapper.delete(tunnelId);
    }

    @Override
    public Tunnel info(Integer tunnelId) {
        return tunnelMapper.info(tunnelId);
    }
}
