package com.cowave.sys.job.infra.route.impl;

import com.cowave.commons.client.http.constants.HttpCode;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.IdleCheck;
import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.infra.route.ClientRouter;
import com.cowave.sys.job.infra.ClientService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
public class ClientRouteBusyOver implements ClientRouter {

    @Override
    public String route(ClientService clientService, TriggerRequest triggerParam, List<String> addressList) {
        for (String address : addressList) {
            try {
                Response<String> response = clientService.idle(address, new IdleCheck(triggerParam.getTriggerId()));
                if (Objects.equals(response.getCode(), HttpCode.SUCCESS.getCode())) {
                    return address;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return null;
    }
}
