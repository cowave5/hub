package com.cowave.sys.job.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Data
public class TriggerContext {

    private static final InheritableThreadLocal<TriggerContext> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static void set(TriggerContext triggerContext){
        CONTEXT_HOLDER.set(triggerContext);
    }

    public static TriggerContext get(){
        return CONTEXT_HOLDER.get();
    }

    private final long jobId;

    private final String jobParam;

    private final int shardIndex;

    private final int shardTotal;

    private Date handleTime;

    private int handleStatus;

    private long handleCost;

    private String handleMsg;
}
