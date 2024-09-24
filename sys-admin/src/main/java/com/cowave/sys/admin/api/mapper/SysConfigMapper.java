/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysConfigMapper {

    /**
     * 列表
     */
    Page<SysConfig> list(Page<SysConfig> page, @Param("config") SysConfig sysConfig);

    /**
     * 详情
     */
    SysConfig info(Integer configId);

    /**
     * 新增
     */
    void insert(SysConfig sysConfig);

    /**
     * 修改
     */
    void update(SysConfig sysConfig);

    /**
     * 多选
     */
    List<SysConfig> choose(Integer[] array);

    /**
     * 删除
     */
    void delete(Integer[] array);
}
