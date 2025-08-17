package com.cowave.sys.job.infra.route;

import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.infra.ClientService;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public interface ClientRouter {

    String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList);
}
