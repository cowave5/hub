/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
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
