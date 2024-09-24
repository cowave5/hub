/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.entity.task.TaskUsage;
import com.cowave.sys.resource.api.mapper.TaskUsageMapper;
import com.cowave.sys.resource.api.mapper.UsageMapper;
import com.cowave.sys.resource.api.service.TaskUsageService;
import com.cowave.sys.resource.api.service.UsageService;
import com.cowave.sys.resource.doc.UsageOption;
import com.cowave.sys.resource.doc.UsageStatus;
import com.cowave.sys.resource.doc.UsageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class TaskUsageServiceImpl implements TaskUsageService {

    private final TaskUsageMapper taskUsageMapper;

    private final UsageMapper usageMapper;

    private final UsageService usageService;

    @Override
    public List<ResourceUsage> list(String taskId, boolean latest) {
        Long usageId = null;
        if(latest){
            usageId = taskUsageMapper.latestUsageId(taskId);
        }
        return taskUsageMapper.list(taskId, usageId);
    }

    @Override
    public List<ResourceUsage> historyList(String taskId) {
        return taskUsageMapper.historyList(taskId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ResourceUsage> allocate(TaskUsage taskUsage) {
        ResourceUsage allocateUsage = new ResourceUsage();
        allocateUsage.setTaskId(taskUsage.getTaskId());
        allocateUsage.setTaskName(taskUsage.getTaskName());
        allocateUsage.setNetNo(taskUsage.getNetNo());
        allocateUsage.setUsageStatus(UsageStatus.ALLOCATE.getCode());
        allocateUsage.setUsageType(UsageType.TASK.getCode());
        allocateUsage.setUsageId(usageMapper.nextUsageId());
        List<ResourceUsage> resources = new ArrayList<>();
        for (Usage usage : taskUsage.getUsages()) {
            resources.add(usageService.allocateUsage(allocateUsage, usage, null, null, taskUsage));
        }
        return resources;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void release(String taskId) {
        if(taskUsageMapper.statusForward(taskId, null, UsageStatus.RELEASE.getCode(), Access.accessUser()) > 0){
            taskUsageMapper.checkReleasePool(taskId);
        }
        if(taskUsageMapper.moveHistory(taskId) > 0){
            taskUsageMapper.clean(taskId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ResourceUsage> adjust(TaskUsage taskUsage) {
        Long latestUsageId = taskUsageMapper.latestUsageId(taskUsage.getTaskId());
        Asserts.notNull(taskUsage.getUsageId(), "usageId不能为空");
        Asserts.notNull(latestUsageId, "任务没有可调整的记录");
        Asserts.equals(latestUsageId, taskUsage.getUsageId(), "任务资源已被修改，请重新确认");

        taskUsageMapper.statusForward(taskUsage.getTaskId(), latestUsageId, UsageStatus.ADJUSTING.getCode(), taskUsage);
        List<ResourceUsage> prevUsages = taskUsageMapper.list(taskUsage.getTaskId(), taskUsage.getUsageId());

        int prevTimes = prevUsages.get(0).getPrevTimes() + 1;
        long usageId = usageMapper.nextUsageId();

        ResourceUsage adjustUsage = new ResourceUsage();
        adjustUsage.setTaskId(taskUsage.getTaskId());
        adjustUsage.setTaskName(taskUsage.getTaskName());
        adjustUsage.setNetNo(taskUsage.getNetNo());
        adjustUsage.setPrevUsageId(taskUsage.getUsageId());
        adjustUsage.setPrevStatus(UsageStatus.ADJUSTING.getCode());
        adjustUsage.setPrevTimes(prevTimes);
        adjustUsage.setUsageId(usageId);
        adjustUsage.setUsageStatus(UsageStatus.ALLOCATE.getCode());

        Set<Long> changedId = new HashSet<>();
        for(Usage usage : taskUsage.getUsages()){
            if (UsageOption.ADD.getCode() == usage.getOption()) {    // 新增
                adjustUsage.setPrevOption(UsageOption.ADD.getCode());
                usageService.allocateUsage(adjustUsage, usage, taskUsage.getTaskId(), taskUsage.getNetNo(), taskUsage);
            }
            if (UsageOption.UPDATE.getCode() == usage.getOption()) { // 调整
                Asserts.notNull(usage.getId(), "调整使用资源, id不能为空");
                changedId.add(usage.getId());
                adjustUsage.setPrevId(usage.getId());
                adjustUsage.setPrevOption(UsageOption.UPDATE.getCode());
                usageService.allocateUsage(adjustUsage, usage, taskUsage.getTaskId(), taskUsage.getNetNo(), taskUsage);
            }
            if (UsageOption.DELETE.getCode() == usage.getOption()) { // 删除
                Asserts.notNull(usage.getId(), "删除使用资源, id不能为空");
                changedId.add(usage.getId());
            }
        }

        for (ResourceUsage prevUsage : prevUsages) {
            if (!changedId.contains(prevUsage.getId())) {  // 不变
                prevUsage.setPrevId(prevUsage.getId());
                prevUsage.setPrevUsageId(prevUsage.getUsageId());
                prevUsage.setPrevStatus(UsageStatus.ADJUSTING.getCode());
                prevUsage.setPrevTimes(prevTimes);
                adjustUsage.setPrevOption(UsageOption.REMAINED.getCode());
                prevUsage.setUsageId(usageId);
                prevUsage.setUsageStatus(UsageStatus.ALLOCATE.getCode());
                usageMapper.createUsage(prevUsage, taskUsage);
            }
        }
        return taskUsageMapper.list(taskUsage.getTaskId(), usageId);
    }
}
