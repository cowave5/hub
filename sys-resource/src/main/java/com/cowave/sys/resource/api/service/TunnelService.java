/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.Tunnel;

/**
 *
 * @author shanhuiming
 *
 */
public interface TunnelService {

    /**
     * 新增
     */
    void add(Tunnel tunnel);

    /**
     * 修改
     */
    void edit(Tunnel tunnel);

    /**
     * 删除
     */
    void remove(Integer tunnelId);

    /**
     * 详情
     */
    Tunnel info(Integer tunnelId);
}
