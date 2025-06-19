package com.cowave.sys.job.infra.route.impl;

import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.infra.route.ClientRouter;
import com.cowave.sys.job.infra.ClientService;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
public class ClientRouteFirst implements ClientRouter {

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList){
        return addressList.get(0);
    }
}
