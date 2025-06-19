package com.cowave.sys.job.app.runner;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.StateMachine;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.LeaderChangeContext;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.cowave.sys.job.infra.schedule.ScheduleContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleMachine implements StateMachine {

    private final ScheduleContext scheduleContext;

    @Override
    public void onLeaderStart(long l) {
        scheduleContext.start();

    }

    @Override
    public void onLeaderStop(Status status) {
        scheduleContext.stop();
    }

    @Override
    public void onApply(Iterator iterator) {
        // no task
    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void onSnapshotSave(SnapshotWriter snapshotWriter, Closure closure) {

    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader snapshotReader) {
        return false;
    }

    @Override
    public void onError(RaftException e) {

    }

    @Override
    public void onConfigurationCommitted(Configuration configuration) {

    }

    @Override
    public void onStopFollowing(LeaderChangeContext leaderChangeContext) {

    }

    @Override
    public void onStartFollowing(LeaderChangeContext leaderChangeContext) {

    }
}
