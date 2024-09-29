/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.quartz.api.qrtz.controller;

import com.cowave.sys.quartz.api.qrtz.entity.QuartzDetail;
import com.cowave.sys.quartz.api.qrtz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * quartz管理
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quartz")
public class QuartzController {

    private final QuartzService quartzService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    public List<QuartzDetail> list() throws SchedulerException {
        return quartzService.list();
    }

    /**
     * 新增
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     * @param cron     cron计划
     */
    @GetMapping(value = "/add")
    public void add(String jobClass, String jobGroup,String cron) throws Exception {
        quartzService.createCron(jobClass, jobClass, jobGroup, cron);
    }

    /**
     * 更新
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     * @param cron     cron计划
     */
    @GetMapping(value = "/update")
    public void rescheduleJob(String jobClass, String jobGroup, String cron) throws Exception {
        quartzService.updateCron(jobClass, jobGroup, cron);
    }

    /**
     * 执行
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     */
    @GetMapping(value = "/exec")
    public void exec(String jobClass, String jobGroup) throws Exception {
        quartzService.exec(jobClass, jobGroup, null);
    }

    /**
     * 暂停
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     */
    @GetMapping(value = "/pause")
    public void pause(String jobClass, String jobGroup) throws Exception {
        quartzService.pause(jobClass, jobGroup);
    }

    /**
     * 恢复
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     */
    @PostMapping(value = "/resume")
    public void resume(String jobClass, String jobGroup) throws Exception {
        quartzService.resume(jobClass, jobGroup);
    }

    /**
     * 删除
     * @param jobClass 作业实现类
     * @param jobGroup 作业分组
     */
    @PostMapping(value = "/delete")
    public void deletejob(String jobClass, String jobGroup) throws Exception {
        quartzService.delete(jobClass, jobGroup);
    }
}
