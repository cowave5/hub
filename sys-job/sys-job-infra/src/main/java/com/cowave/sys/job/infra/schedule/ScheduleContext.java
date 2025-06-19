package com.cowave.sys.job.infra.schedule;

import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.infra.dao.JobTriggerDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.cowave.sys.job.infra.schedule.MisfireStrategyEnum.FIRE_NOW;
import static com.cowave.sys.job.infra.schedule.ScheduleTypeEnum.CRON;
import static com.cowave.sys.job.infra.schedule.ScheduleTypeEnum.FIX_RATE;
import static com.cowave.sys.job.infra.schedule.TriggerTypeEnum.MISFIRE;
import static com.cowave.sys.job.infra.schedule.TriggerTypeEnum.SCHEDULE;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleContext {
    private static final Map<Integer, Set<Integer>> RING_DATA = new ConcurrentHashMap<>();
    private static final long PRE_READ_MS = 5000;
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStop = false;
    private Thread scheduleThread;
    private Thread ringThread;
    private final ScheduleTriggerExecutor scheduleTriggerExecutor;
    private final JobTriggerDao jobTriggerDao;

    public void start() {
        scheduleTriggerExecutor.start();

        scheduleThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(PRE_READ_MS - System.currentTimeMillis() % 1000);
            } catch (Throwable e) {
                if (!scheduleThreadToStop) {
                    log.error("", e);
                }
            }

            // pre-read count: treadpool-size * trigger-qps (each trigger cost 50ms, qps = 1000/50 = 20)
            int preReadCount = 20 * 20;
            while (!scheduleThreadToStop) {
                try {
                    // 1、pre read
                    long now = System.currentTimeMillis();
                    List<JobTrigger> triggerList = jobTriggerDao.preListInSeconds(now + PRE_READ_MS, preReadCount);
                    if (!triggerList.isEmpty()) {
                        for (JobTrigger trigger : triggerList) {
                            // 任务开始时间
                            Date taskBeginTime = trigger.getTaskBeginTime();
                            if(taskBeginTime != null && now < taskBeginTime.getTime()){
                                continue;
                            }

                            long nextTime = trigger.getTriggerNextTime();
                            // 任务截止时间
                            Date taskEndTime = trigger.getTaskEndTime();
                            if(taskEndTime != null && nextTime > taskEndTime.getTime()){
                                continue;
                            }

                            if (now > nextTime + PRE_READ_MS) {
                                // 过期超过5s
                                if (FIRE_NOW.equalsName(trigger.getMisfireStrategy())) {
                                    scheduleTriggerExecutor.addTrigger(trigger.getId(), MISFIRE, null, null);
                                }
                                refreshNextValidTime(trigger, new Date());
                            } else if (now > nextTime) {
                                // 过期小于5s
                                scheduleTriggerExecutor.addTrigger(trigger.getId(), SCHEDULE, null, null);
                                refreshNextValidTime(trigger, new Date());
                                // next-trigger-time in 5s, pre-read again
                                if (trigger.getStatus() == 1 && now + PRE_READ_MS > nextTime) {
                                    // 1、make ring second
                                    int ringSecond = (int) ((nextTime / 1000) % 60);
                                    // 2、push time ring
                                    pushTimeRing(ringSecond, trigger.getId());
                                    // 3、fresh next
                                    refreshNextValidTime(trigger, new Date(nextTime));
                                }
                            } else {
                                // 未过期
                                // 1、make ring second
                                int ringSecond = (int) ((trigger.getTriggerNextTime() / 1000) % 60);
                                // 2、push time ring
                                pushTimeRing(ringSecond, trigger.getId());
                                // 3、fresh next
                                refreshNextValidTime(trigger, new Date(trigger.getTriggerNextTime()));
                            }
                        }
                        // 3、update trigger info
                        for (JobTrigger trigger : triggerList) {
                            jobTriggerDao.updateTriggerTime(trigger);
                        }
                    }
                    // tx stop
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (Throwable e) {
                    if (!scheduleThreadToStop) {
                        log.error("", e);
                    }
                }
            }
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("job-scheduler");
        scheduleThread.start();

        // ring thread
        ringThread = new Thread(() -> {
            int lastSecond = Calendar.getInstance().get(Calendar.SECOND);
            while (!ringThreadToStop) {
                int currentSecond = Calendar.getInstance().get(Calendar.SECOND);
                if (lastSecond != currentSecond) {
                    lastSecond = currentSecond;
                    try {
                        Set<Integer> list = RING_DATA.remove(currentSecond);
                        if (list != null) {
                            for (int jobId : list) {
                                scheduleTriggerExecutor.addTrigger(jobId, SCHEDULE, null, null);
                            }
                        }
                    } catch (Throwable e) {
                        if (!ringThreadToStop) {
                            log.error("", e);
                        }
                    }
                } else {
                    try {
                        // 这里有个put/get的先后顺序问题，没有特别好的办法，将get操作往后偏100ms，如果put错过窗口只能下次get
                        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                    } catch (Throwable e) {
                        if (!ringThreadToStop) {
                            log.error("", e);
                        }
                    }
                }
            }
        });
        ringThread.setDaemon(true);
        ringThread.setName("job-ring");
        ringThread.start();
    }

    private void refreshNextValidTime(JobTrigger jobTrigger, Date fromTime) {
        try {
            Date nextValidTime = generateNextValidTime(jobTrigger, fromTime);
            if (nextValidTime != null) {
                jobTrigger.setStatus(-1);   // pass, may be Inaccurate
                jobTrigger.setTriggerLastTime(jobTrigger.getTriggerNextTime());
                jobTrigger.setTriggerNextTime(nextValidTime.getTime());
            } else {
                // generateNextValidTime fail, stop job
                jobTrigger.setStatus(0);
                jobTrigger.setTriggerLastTime(0);
                jobTrigger.setTriggerNextTime(0);
            }
        } catch (Throwable e) {
            jobTrigger.setStatus(0);
            jobTrigger.setTriggerLastTime(0);
            jobTrigger.setTriggerNextTime(0);
            log.error("", e);
        }
    }

    private void pushTimeRing(int ringSecond, int jobId) {
        Set<Integer> ringItemData = RING_DATA.computeIfAbsent(ringSecond, k -> new HashSet<>());
        ringItemData.add(jobId);
    }

    public void stop() {
        // 1、stop schedule
        scheduleThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Throwable e) {
            log.error("", e);
        }

        if (scheduleThread.getState() != Thread.State.TERMINATED) {
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (Throwable e) {
                log.error("", e);
            }
        }

        // if has ring data
        boolean hasRingData = false;
        if (!RING_DATA.isEmpty()) {
            for (int second : RING_DATA.keySet()) {
                Set<Integer> tmpData = RING_DATA.get(second);
                if (tmpData != null && !tmpData.isEmpty()) {
                    hasRingData = true;
                    break;
                }
            }
        }
        if (hasRingData) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (Throwable e) {
                log.error("", e);
            }
        }

        // stop ring (wait job-in-memory stop)
        ringThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Throwable e) {
            log.error("", e);
        }
        if (ringThread.getState() != Thread.State.TERMINATED) {
            ringThread.interrupt();
            try {
                ringThread.join();
            } catch (Throwable e) {
                log.error("", e);
            }
        }
        scheduleTriggerExecutor.stop();
    }

    public static Date generateNextValidTime(JobTrigger jobInfo, Date fromTime) throws Exception {
        if (CRON.equalsName(jobInfo.getTriggerType())) {
            return new CronExpression(jobInfo.getTriggerParam()).getNextValidTimeAfter(fromTime);
        } else if (FIX_RATE.equalsName(jobInfo.getTriggerType())) {
            return new Date(fromTime.getTime() + Long.parseLong(jobInfo.getTriggerParam()) * 1000L);
        }
        return null;
    }
}
