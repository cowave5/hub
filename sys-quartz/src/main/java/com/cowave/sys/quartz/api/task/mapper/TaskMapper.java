/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.mapper;

import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface TaskMapper {

    /**
     * 列表
     */
    List<QuartzTask> list(QuartzTask job);

    /**
     * 详情
     */
    QuartzTask info(Long id);

    /**
     * 修改状态
     */
    void changeStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 累计次数
     */
    void increase(Long id);

    /**
     * 新增
     */
    int insert(QuartzTask task);

    /**
     * 更新
     */
    int update(QuartzTask task);

    /**
     * 删除
     */
    int delete(Long id);
}
