package com.cowave.sys.job.client;

import com.cowave.commons.client.http.annotation.HttpClient;
import com.cowave.commons.client.http.annotation.HttpHost;
import com.cowave.commons.client.http.annotation.HttpLine;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.TriggerResponse;
import com.cowave.sys.job.domain.client.ClientRegistry;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@HttpClient
public interface ServerService {

    @HttpLine("POST /job/client/registry")
    Response<Void> registry(@HttpHost String httpUrl, ClientRegistry clientRegistry);

    @HttpLine("POST /job/client/unregistry")
    Response<Void> unRegistry(@HttpHost String httpUrl, ClientRegistry clientRegistry);

    @HttpLine("POST /job/client/callback")
    void callback(@HttpHost String httpUrl, List<TriggerResponse> list);
}
