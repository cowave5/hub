/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.sys.model.resource.Tunnel;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface TunnelMapper {

    /**
     * 新增
     */
    void insert(Tunnel tunnel);

    /**
     * 修改
     */
    void update(Tunnel tunnel);

    /**
     * 同步修改上行资源
     */
    void updateUpPool(Tunnel tunnel);

    /**
     * 同步修改下行资源
     */
    void updateDownPool(Tunnel tunnel);

    /**
     * 删除
     */
    void delete(Integer tunnelId);

    /**
     * 使用此通道的转发器名称
     */
    String existTspdName(Integer tunnelId);

    /**
     * 详情
     */
    Tunnel info(Integer tunnelId);
}
