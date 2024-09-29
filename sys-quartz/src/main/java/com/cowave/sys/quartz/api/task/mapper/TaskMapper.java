/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
