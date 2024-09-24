/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.model.resource.Transponder;
import com.cowave.sys.resource.api.entity.task.Usage;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface TransponderService {

    /**
     * 新增
     */
    void add(Transponder transponder);

    /**
     * 修改
     */
    void edit(Transponder transponder);

    /**
     * 删除
     */
    void remove(Integer tspdId);

    /**
     * 详情
     */
    Transponder info(Integer tspdId);

    /**
     * 卫星转发器
     */
    List<Transponder> satelliteQuery(Integer satId);

    /**
     * 波束转发器
     */
    List<Transponder> beamQuery(Integer beamId);

    /**
     * 通道转发器
     */
    List<Transponder> tunnelQuery(Integer tunnelId);

    /**
     * 转发器资源
     */
    List<ResourcePool> poolList(Integer tspdId);

    /**
     * 资源使用记录
     */
    List<ResourceUsage> usageList(Integer tspdId);

    /**
     * 转发器指定时间内使用记录
     */
    List<ResourceUsage> timeUsageList(Usage usage);
}
