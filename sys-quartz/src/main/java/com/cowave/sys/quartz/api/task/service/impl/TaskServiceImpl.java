/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.task.service.impl;

import com.cowave.sys.quartz.api.task.entity.QuartzTask;
import com.cowave.sys.quartz.api.task.mapper.TaskMapper;
import com.cowave.sys.quartz.api.task.service.TaskService;
import com.cowave.sys.quartz.api.qrtz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 *
 * @author  shanhuiming
 */
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final QuartzService quartz;

    private final TaskMapper taskMapper;

    @PostConstruct
    public void init() throws SchedulerException {
        List<QuartzTask> list = taskMapper.list(new QuartzTask());
        for (QuartzTask task : list) {
            cronSchedule(task);
        }
    }

    @Override
    public void refresh() throws SchedulerException {
        quartz.clear();
        init();
    }

    @Override
    public List<QuartzTask> list(QuartzTask job) {
        return taskMapper.list(job);
    }

    @Override
    public QuartzTask info(Long id) {
        return taskMapper.info(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(QuartzTask quartzTask) throws SchedulerException {
        quartzTask.setStatus(1);
        if (taskMapper.insert(quartzTask) > 0) {
            cronSchedule(quartzTask);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(QuartzTask quartzTask) throws SchedulerException {
        QuartzTask prevTask = info(quartzTask.getId());
        if (taskMapper.update(quartzTask) > 0) {
            quartz.delete(prevTask.getTaskName(), prevTask.getTaskGroup()); // 删掉之前的
            quartzTask.setStatus(prevTask.getStatus()); // status
            cronSchedule(quartzTask);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] ids) throws SchedulerException {
        for (Long id : ids) {
            QuartzTask task = taskMapper.info(id);
            delete(task);
        }
    }

    private void delete(QuartzTask task) throws SchedulerException {
        if (taskMapper.delete(task.getId()) > 0) {
            quartz.delete(task.getTaskName(), task.getTaskGroup());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exec(Long id) throws SchedulerException {
        QuartzTask task = info(id);
        JobDataMap jobData = new JobDataMap();
        jobData.put(QuartzTask.JOB_PROPERTIES, task);
        quartz.exec(task.getTaskName(), task.getTaskGroup(), jobData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) throws SchedulerException {
        QuartzTask task = info(id);
        taskMapper.changeStatus(id, status);
        if(status == 0){
            quartz.pause(task.getTaskName(), task.getTaskGroup());
        }else if(status == 1){
            quartz.resume(task.getTaskName(), task.getTaskGroup());
        }
    }

    private void cronSchedule(QuartzTask quartzTask) throws SchedulerException {
        Class<? extends Job> jobClass = quartzTask.ofJobClass();
        String jobName = quartzTask.getTaskName();
        String jobGroup = quartzTask.getTaskGroup();
        JobDetail jobDetail = quartz.jobDetail(jobClass, jobName, jobGroup);
        jobDetail.getJobDataMap().put(QuartzTask.JOB_PROPERTIES, quartzTask); // job参数

        TriggerBuilder<CronTrigger> builder = TriggerBuilder.newTrigger()
                .withIdentity(quartz.triggerKey(jobName, jobGroup))
                .withPriority(quartzTask.getPriority())
                .withSchedule(quartzTask.ofCronScheduleBuilder());
        Date beginTime = quartzTask.getBeginTime();
        Date endTime = quartzTask.getEndTime();
        if(beginTime != null){
            builder.startAt(beginTime);
        }
        if(endTime != null){
            builder.endAt(endTime);
        }
        CronTrigger trigger = builder.build();

        if (quartz.jobExists(jobName, jobGroup)){
            quartz.reschedule(jobName, jobGroup, trigger);
        }else{
            quartz.scheduleJob(jobDetail, trigger);
        }

        if(quartzTask.getStatus() == 0){
            quartz.pause(jobName, jobGroup);
        }else{
            quartz.resume(jobName, jobGroup);
        }
    }
}
