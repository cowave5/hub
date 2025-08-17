package com.cowave.sys.job.app.runner;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.cowave.sys.job.app.configuration.RaftConfiguration;
import com.cowave.sys.job.infra.schedule.ScheduleContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleRunner implements ApplicationRunner {
    private final RaftConfiguration raftConfiguration;
    private final ScheduleMachine scheduleMachine;
    private final ScheduleContext scheduleContext;
    private Node node;

    @Override
    public void run(ApplicationArguments args) {
        if (!raftConfiguration.isEnable()) {
            scheduleContext.start();
        } else {
            // 集群节点
            Configuration configuration = JRaftUtils.getConfiguration(raftConfiguration.getNodes());
            // 当前节点设置
            NodeOptions nodeOptions = new NodeOptions();
            nodeOptions.setInitialConf(configuration);
            nodeOptions.setFsm(scheduleMachine);
            nodeOptions.setLogUri("log/data");
            nodeOptions.setRaftMetaUri("log/meta");
            nodeOptions.setSnapshotUri("log/snapshot");
            node = new RaftGroupService(
                    "default", JRaftUtils.getPeerId(raftConfiguration.getCurrentNode()), nodeOptions).start();
        }
    }

    @PreDestroy
    public void stop() {
        scheduleContext.stop();
        node.shutdown();
    }
}
