/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service;

import com.cowave.sys.model.resource.Beam;

/**
 *
 * @author shanhuiming
 *
 */
public interface BeamService {

    /**
     * 新增
     */
    void add(Beam beam);

    /**
     * 修改
     */
    void edit(Beam beam);

    /**
     * 删除
     */
    void remove(Integer beamId);

    /**
     * 详情
     */
    Beam info(Integer beamId);
}
