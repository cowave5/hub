/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysConfig;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysConfigService {

    /**
     * 列表
     */
    Page<SysConfig> list(SysConfig sysConfig);

    /**
     * 详情
     */
    SysConfig info(Integer configId);

    /**
     * 新增
     */
    void add(SysConfig sysConfig) throws Exception;

    /**
     * 编辑
     */
    void edit(SysConfig sysConfig) throws Exception;

    /**
     * 删除
     */
    void delete(Integer[] configId) throws Exception;
}
