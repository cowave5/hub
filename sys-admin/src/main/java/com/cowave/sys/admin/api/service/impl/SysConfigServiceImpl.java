/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.caches.SysConfigCaches;
import com.cowave.sys.admin.api.mapper.SysConfigMapper;
import com.cowave.sys.admin.api.service.SysConfigService;
import com.cowave.sys.model.admin.SysConfig;
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
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigCaches sysConfigCaches;

    private final SysConfigMapper sysConfigMapper;

    @Override
    public Page<SysConfig> list(SysConfig sysConfig) {
        return sysConfigMapper.list(Access.page(), sysConfig);
    }

    @Override
    public SysConfig info(Integer configId) {
        return sysConfigMapper.info(configId);
    }

    @Override
    public void add(SysConfig sysConfig) throws Exception {
        sysConfigMapper.insert(sysConfig);
        sysConfigCaches.putValue(sysConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysConfig sysConfig) throws Exception {
        // 根据id修改，configKey可以改变
        Asserts.notNull(sysConfig.getConfigId(), "{config.notnull.id}");
        SysConfig preConfig = sysConfigMapper.info(sysConfig.getConfigId());
        Asserts.notNull(preConfig, "{config.notexist}", sysConfig.getConfigId());
        // 校验parser
        sysConfigMapper.update(sysConfig);
        sysConfigCaches.putValue(sysConfig);
    }

    @Override
    public void delete(Integer[] configId) throws Exception {
        List<SysConfig> list = sysConfigMapper.choose(configId);
        sysConfigMapper.delete(configId);
        for(SysConfig conf : list) {
            sysConfigCaches.delete(conf);
        }
    }
}
