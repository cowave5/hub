/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.mapper;

import com.cowave.sys.quartz.api.task.entity.QuartzLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface TaskLogMapper {

    /**
     * 列表
     */
    List<QuartzLog> list(QuartzLog quartzLog);

    /**
     * 新增
     */
    int insert(QuartzLog jobLog);

    /**
     * 删除
     */
    int delete(Long[] array);

    /**
     * 清空
     */
    void clean();
}
