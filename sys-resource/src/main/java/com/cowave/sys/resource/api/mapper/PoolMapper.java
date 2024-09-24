/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.resource.api.entity.task.Usage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface PoolMapper {

    /**
     * 转发器生成资源
     */
    void createByTspd(ResourcePool pool);

    /**
     * 填充卫星信息
     */
    void fillSatellite(Long id);

    /**
     * 填充上行波束信息
     */
    void fillUpBeam(Long id);

    /**
     * 填充下行波束信息
     */
    void fillDownBeam(Long id);

    /**
     * 填充上行通道信息
     */
    void fillUpTunnel(Long id);

    /**
     * 填充下行通道信息
     */
    void fillDownTunnel(Long id);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 计数：资源使用记录数
     */
    int countPoolUsages(Long srcId);

    /**
     * 资源树
     */
    List<ResourcePool> treePools();

    /**
     * 转发器资源
     */
    List<ResourcePool> queryByTspdId(Integer tspdId);

    /**
     * 可用资源记录(时间上包含，频段上有交集)
     */
    List<ResourcePool> availablePool(Usage usage);

    /**
     * 更新状态
     */
    void updateStatus(@Param("srcId") Long srcId, @Param("srcStatus") Integer srcStatus);
}
