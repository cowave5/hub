package com.cowave.sys.job.service.impl;

import com.cowave.commons.tools.Collections;
import com.cowave.sys.job.domain.JobClient;
import com.cowave.sys.job.domain.JobClientHandler;
import com.cowave.sys.job.domain.enums.JobTriggerStatus;
import com.cowave.sys.job.domain.client.ClientRegistry;
import com.cowave.sys.job.domain.client.TriggerResponse;
import com.cowave.sys.job.infra.dao.JobClientDao;
import com.cowave.sys.job.infra.dao.JobClientHandlerDao;
import com.cowave.sys.job.infra.dao.JobTriggerDao;
import com.cowave.sys.job.infra.dao.JobTriggerLogDao;
import com.cowave.sys.job.service.JobClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class JobClientServiceImpl implements JobClientService {
    private final JobClientDao jobClientDao;
    private final JobTriggerDao jobTriggerDao;
    private final JobTriggerLogDao jobTriggerLogDao;
    private final JobClientHandlerDao jobClientHandlerDao;

    @Override
    public void registry(ClientRegistry clientRegistry) {
        JobClient jobClient = new JobClient(clientRegistry);
        Integer clientId = jobClientDao.registry(jobClient);
        jobClientHandlerDao.removeByClientId(clientId);
        List<JobClientHandler> handlerList = Collections.copyToList(
                clientRegistry.getHandlerList(), h -> new JobClientHandler(clientId, h));
        jobClientHandlerDao.saveBatch(handlerList);
    }

    @Override
    public void unRegistry(ClientRegistry clientRegistry) {
        JobClient jobClient = new JobClient(clientRegistry);
        Integer clientId = jobClientDao.unRegistry(jobClient);
        jobClientHandlerDao.removeByClientId(clientId);
    }

    @Override
    public void callback(List<TriggerResponse> list) {
        for(TriggerResponse triggerResponse : list){
            jobTriggerLogDao.completeTriggerLog(triggerResponse);
            if(JobTriggerStatus.EXEC_SUCCESS.getStatus() ==  triggerResponse.getHandleStatus()){
                jobTriggerDao.increaseTriggerSuccessTimes(triggerResponse.getTriggerId());
            }
        }
    }
}
