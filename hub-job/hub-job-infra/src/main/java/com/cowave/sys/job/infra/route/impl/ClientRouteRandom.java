package com.cowave.sys.job.infra.route.impl;

import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.infra.route.ClientRouter;
import com.cowave.sys.job.infra.ClientService;

import java.util.List;
import java.util.Random;

/**
 * @author xuxueli/shanhuiming
 */
public class ClientRouteRandom implements ClientRouter {

    private static final Random LOCAL_RANDOM = new Random();

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        return addressList.get(LOCAL_RANDOM.nextInt(addressList.size()));
    }
}
