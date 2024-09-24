/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import java.util.List;

import com.cowave.sys.admin.api.entity.flow.FlowExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface FlowExecutionMapper {

    /**
     * 详情
     */
    FlowExecution selectActRuExecutionById(String id);

    /**
     * 列表
     */
    List<FlowExecution> selectActRuExecutionList(FlowExecution actRuExecution);

    /**
     * 名称查询
     */
    List<FlowExecution> selectActRuExecutionListByProcessName(@Param("name") String name);

    /**
     * 新增
     */
    int insertActRuExecution(FlowExecution actRuExecution);

    /**
     * 修改
     */
    int updateActRuExecution(FlowExecution actRuExecution);

    /**
     * 删除
     */
    int deleteActRuExecutionById(String id);

    /**
     * 批量删除
     */
    int deleteActRuExecutionByIds(String[] ids);
}
