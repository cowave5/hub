/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.sys.model.resource.Transponder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface TransponderMapper {

    /**
     * 新增
     */
    void insert(Transponder transponder);

    /**
     * 更新
     */
    void update(Transponder transponder);

    /**
     * 同步修改资源
     */
    void updatePool(Transponder transponder);

    /**
     * 检查卫星存在
     */
    Integer ensureSatellite(Integer satId);

    /**
     * 检查波束存在
     */
    Integer ensureBeam(Integer beamId);

    /**
     * 检查通道存在
     */
    Integer ensureTunnel(Integer tunnelId);

    /**
     * 更新频差
     */
    void updateFreqDiff(@Param("tspdId") Integer tspdId,
                        @Param("upTunnelId") Integer upTunnelId, @Param("downTunnelId") Integer downTunnelId);

    /**
     * 删除
     */
    void delete(Integer tspdId);

    /**
     * 详情
     */
    Transponder info(Integer tspdId);

    /**
     * 是否存在于资源池
     */
    Long existInPool(Integer tspdId);

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
}
