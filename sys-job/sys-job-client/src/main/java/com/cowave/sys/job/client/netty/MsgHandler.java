package com.cowave.sys.job.client.netty;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.IdleCheck;
import com.cowave.sys.job.domain.client.KillRequest;
import com.cowave.sys.job.domain.client.TriggerRequest;

/**
 * @author xuxueli/shanhuiming
 */
public interface MsgHandler {

    Response<Void> beat();

    Response<Void> checkIdle(IdleCheck idleCheck);

    Response<String> exec(TriggerRequest triggerRequest);

    Response<Void> kill(KillRequest killRequest);
}
