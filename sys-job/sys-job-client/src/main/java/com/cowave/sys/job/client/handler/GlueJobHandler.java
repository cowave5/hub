package com.cowave.sys.job.client.handler;

import com.cowave.sys.job.client.TriggerContext;
import com.cowave.sys.job.domain.constant.TriggerStatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
public class GlueJobHandler implements JobHandler {

    private final JobHandler jobHandler;

    @Getter
    private final long glueUpdatetime;

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        TriggerContext.get().setHandleTime(new Date());
        try {
            jobHandler.execute();
            TriggerContext.get().setHandleStatus(TriggerStatusEnum.EXEC_SUCCESS.getStatus());
        } catch (Exception e) {
            log.error("", e);
            TriggerContext.get().setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
            TriggerContext.get().setHandleMsg(e.getMessage());
        }
        TriggerContext.get().setHandleCost(System.currentTimeMillis() - startTime);
    }

    @Override
    public void init() throws Exception {
        this.jobHandler.init();
    }

    @Override
    public void destroy() throws Exception {
        this.jobHandler.destroy();
    }
}
