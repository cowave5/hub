package com.cowave.sys.job.infra.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTriggerExecutor {

    private final ConcurrentMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();

    private final ScheduleTrigger scheduleTrigger;

    private volatile long minTim = System.currentTimeMillis() / 60000;

    private ThreadPoolExecutor fastTriggerPool = null;

    private ThreadPoolExecutor slowTriggerPool = null;

    public void start(){
        fastTriggerPool = new ThreadPoolExecutor(10, 10, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2000),
                new CustomizableThreadFactory("job-trigger1-"),
                (r, executor) -> log.error("reject {}", r.toString()));

        slowTriggerPool = new ThreadPoolExecutor(10, 10, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(5000),
                new CustomizableThreadFactory("job-trigger2-"),
                (r, executor) -> log.error("reject {}", r.toString()));
    }

    public void stop() {
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
    }

    public void addTrigger(int jobId, TriggerTypeEnum triggerType, String executorShardingParam, String executorParam) {
        // choose thread pool
        ThreadPoolExecutor executor = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get() > 10) {
            executor = slowTriggerPool;
        }

        // trigger
        executor.execute(() -> {
            long start = System.currentTimeMillis();
            try {
                scheduleTrigger.trigger(jobId, triggerType, executorShardingParam, executorParam);
            } catch (Throwable e) {
                log.error("", e);
            } finally {
                // check timeout-count-map
                long minTimNow = System.currentTimeMillis() / 60000;
                if (minTim != minTimNow) {
                    minTim = minTimNow;
                    jobTimeoutCountMap.clear();
                }

                // incr timeout-count-map
                long cost = System.currentTimeMillis() - start;
                if (cost > 500) {       // ob-timeout threshold 500ms
                    AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId, new AtomicInteger(1));
                    if (timeoutCount != null) {
                        timeoutCount.incrementAndGet();
                    }
                }
            }
        });
    }
}
