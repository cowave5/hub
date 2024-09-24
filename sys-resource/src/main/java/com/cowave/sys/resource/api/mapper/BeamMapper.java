/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.mapper;

import com.cowave.sys.model.resource.Beam;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface BeamMapper {

    /**
     * 新增
     */
    void insert(Beam beam);

    /**
     * 修改
     */
    void update(Beam beam);

    /**
     * 同步修改上行资源
     */
    void updateUpPool(Beam beam);

    /**
     * 同步修改下行资源
     */
    void updateDownPool(Beam beam);

    /**
     * 删除
     */
    void delete(Integer beamId);

    /**
     * 使用此波束的转发器名称
     */
    String existTspdName(Integer beamId);

    /**
     * 详情
     */
    Beam info(Integer beamId);
}
