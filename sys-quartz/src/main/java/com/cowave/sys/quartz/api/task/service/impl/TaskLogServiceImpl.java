/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.service.impl;

import com.cowave.sys.quartz.api.task.entity.QuartzLog;
import com.cowave.sys.quartz.api.task.mapper.TaskLogMapper;
import com.cowave.sys.quartz.api.task.mapper.TaskMapper;
import com.cowave.sys.quartz.api.task.service.TaskLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class TaskLogServiceImpl implements TaskLogService {

    private final TaskMapper taskMapper;

    private final TaskLogMapper taskLogMapper;

    @Override
    public void add(QuartzLog quartzLog) {
        taskLogMapper.insert(quartzLog);
        taskMapper.increase(quartzLog.getTaskId());
    }

    @Override
    public List<QuartzLog> list(QuartzLog quartzLog) {
        return taskLogMapper.list(quartzLog);
    }

    @Override
    public void delete(Long[] id) {
        taskLogMapper.delete(id);
    }

    @Override
    public void clean() {
        taskLogMapper.clean();
    }
}
