package com.cowave.sys.job.infra;

import com.cowave.commons.client.http.annotation.HttpClient;
import com.cowave.commons.client.http.annotation.HttpHost;
import com.cowave.commons.client.http.annotation.HttpLine;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.IdleCheck;
import com.cowave.sys.job.domain.client.KillRequest;
import com.cowave.sys.job.domain.client.TriggerRequest;

/**
 * @author xuxueli/shanhuiming
 */
@HttpClient
public interface ClientService {

    @HttpLine("POST /beat")
    Response<String> beat(@HttpHost String httpUrl);

    @HttpLine("POST /idle")
    Response<String> idle(@HttpHost String httpUrl, IdleCheck idleCheck);

    @HttpLine("POST /exec")
    Response<String> exec(@HttpHost String httpUrl, TriggerRequest triggerRequest);

    @HttpLine("POST /kill")
    Response<String> kill(@HttpHost String httpUrl, KillRequest killRequest);
}
