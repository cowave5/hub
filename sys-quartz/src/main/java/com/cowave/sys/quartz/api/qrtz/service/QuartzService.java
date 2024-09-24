/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.api.qrtz.service;

import com.cowave.sys.quartz.api.qrtz.entity.QuartzDetail;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * CronTrigger 基于Cron表达式的触发器
 * DailyTimeIntervalTrigger 基于日期的触发器，如每天的某个时间段
 * CalendarIntervalTrigger  基于日历的触发器，比简单触发器更多时间单位，且能智能区分大小月和平闰年
 * SimpleTrigger  简单触发器，支持定义任务执行的间隔时间，执行次数的规则有两种，一是定义重复次数，二是定义开始时间和结束时间。
 *                如果同时设置了结束时间与重复次数，先结束的会覆盖后结束的，以先结束的为准
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class QuartzService {

    private final Scheduler scheduler;

    /**
     * cron 存在则更新,否则创建
     *
     * @param className 任务类名
     * @param jobName   任务名称
     * @param jobGroup  任务组名称
     * @param cron      cron表达式
     */
    public void createCron(String className, String jobName, String jobGroup, String cron) throws Exception {
        TriggerKey triggerKey = triggerKey(jobName, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            addCron(className, jobName, jobGroup, cron);
        } else {
            if (trigger.getCronExpression().equals(cron)) {
                return;
            }
            updateCron(jobName, jobGroup, cron);
        }
    }

    /**
     * cron 新增
     *
     * @param jobClass  任务类名
     * @param jobName   任务名称
     * @param jobGroup  任务组名
     * @param cron      cron表达式
     */
    public void addCron(String jobClass, String jobName, String jobGroup, String cron) throws Exception {
        JobDetail jobDetail = jobDetail(getClass(jobClass), jobName, jobGroup);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).startNow().build();
        scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * cron 更新
     *
     * @param jobName   任务名称
     * @param jobGroup  任务组名称
     * @param cron      cron表达式
     */
    public void updateCron(String jobName, String jobGroup, String cron) throws SchedulerException {
        TriggerKey triggerKey = triggerKey(jobName, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        scheduler.rescheduleJob(triggerKey, trigger); // 重启触发器
    }

    /**
     * simple 新增
     *
     * @param jonClass  任务类名
     * @param jobName   任务名称
     * @param jobGroup  任务组名
     * @param intervalInSeconds   间隔时间
     * @param triggerRepeatCount  重复次数
     */
    public void addSimple(String jonClass, String jobName, String jobGroup,
                          int intervalInSeconds, int triggerRepeatCount) throws Exception {
        JobDetail jobDetail = jobDetail(getClass(jonClass), jobName, jobGroup);
        Trigger trigger;
        if (triggerRepeatCount < 0) {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(intervalInSeconds))
                    .startNow().build();
        } else {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(intervalInSeconds)
                    .withRepeatCount(triggerRepeatCount)).startNow().build();
        }
        scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 恢复
     */
    public void reschedule(String jobName, String jobGroup, Trigger trigger) throws SchedulerException {
        scheduler.rescheduleJob(triggerKey(jobName, jobGroup), trigger);
    }

    /**
     * 删除
     */
    public void delete(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey(jobName, jobGroup));
        scheduler.unscheduleJob(triggerKey(jobName, jobGroup));
        scheduler.deleteJob(jobKey(jobName, jobGroup));
    }

    /**
     * 暂停
     */
    public void pause(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey(jobName, jobGroup));
        scheduler.pauseJob(jobKey(jobName, jobGroup));
    }

    /**
     * 恢复
     */
    public void resume(String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey(jobName, jobGroup));
        scheduler.resumeJob(jobKey(jobName, jobGroup));
    }



    /**
     * 执行
     */
    public void exec(String jobName, String jobGroup, JobDataMap jobData) throws SchedulerException {
        scheduler.triggerJob(jobKey(jobName, jobGroup), jobData);
    }

    /**
     * 任务列表
     */
    public List<QuartzDetail> list() throws SchedulerException {
        List<QuartzDetail> list = new ArrayList<>();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                list.add(getDetail(trigger, jobKey));
            }
        }
        return list;
    }

    /**
     * 运行中的任务列表
     */
    public List<QuartzDetail> listRunning() throws SchedulerException {
        List<QuartzDetail> list = new ArrayList<>();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        for (JobExecutionContext executingJob : executingJobs) {
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            list.add(getDetail(trigger, jobKey));
        }
        return list;
    }

    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    public boolean jobExists(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.checkExists(jobKey(jobName, jobGroup));
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException{
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public TriggerKey triggerKey(String jobName, String jobGroup){
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    public CronTrigger cronTrigger(CronScheduleBuilder cronScheduleBuilder, String jobName, String jobGroup, int priority,
                                   Date beginTime, Date endTime){
        return TriggerBuilder.newTrigger().withIdentity(triggerKey(jobName, jobGroup))
                .withPriority(priority).startAt(beginTime).endAt(endTime)
                .withSchedule(cronScheduleBuilder).build();
    }

    public JobKey jobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    public JobDetail jobDetail(Class<? extends org.quartz.Job> jobClass, String jobName, String jobGroup){
        return JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
    }

    private Class<? extends QuartzJobBean> getClass(String className) throws Exception {
        Class<?> class1 = Class.forName(className);
        return (Class<? extends QuartzJobBean>) class1;
    }

    private QuartzDetail getDetail(Trigger trigger, JobKey jobKey) throws SchedulerException {
        QuartzDetail quartzDetail = new QuartzDetail();
        if (trigger instanceof CronTrigger cronTrigger) {
            quartzDetail.setCronExpression(cronTrigger.getCronExpression());
            quartzDetail.setTimeZone(cronTrigger.getTimeZone().getDisplayName());
        }
        quartzDetail.setTriggerGroupName(trigger.getKey().getName());
        quartzDetail.setTriggerName(trigger.getKey().getGroup());
        quartzDetail.setJobGroupName(jobKey.getGroup());
        quartzDetail.setJobName(jobKey.getName());
        quartzDetail.setStartTime(trigger.getStartTime());
        quartzDetail.setJobClassName(scheduler.getJobDetail(jobKey).getJobClass().getName());
        quartzDetail.setNextFireTime(trigger.getNextFireTime());
        quartzDetail.setPreviousFireTime(trigger.getPreviousFireTime());
        quartzDetail.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
        return quartzDetail;
    }
}
