/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.mapper;

import com.cowave.commons.framework.access.AccessUser;
import com.cowave.sys.code.api.entity.ModelInfo;
import com.cowave.sys.code.api.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface ModelMapper {

    /**
     * 模型选项
     */
    List<SelectOption> options();

    /**
     * 列表
     */
    List<ModelInfo> list(ModelInfo modelInfo);

    /**
     * 详情
     */
    ModelInfo info(Long id);

    /**
     * 新增
     */
    void insert(ModelInfo modelInfo);

    /**
     * 编辑
     */
    void update(ModelInfo modelInfo);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * 是否导出Excel切换
     */
    void switchExcel(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否继承Access切换
     */
    void switchAccess(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);

    /**
     * 是否记录日志切换
     */
    void switchLog(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessUser user);
}
