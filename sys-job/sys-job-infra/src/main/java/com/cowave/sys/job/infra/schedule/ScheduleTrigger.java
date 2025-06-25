package com.cowave.sys.job.infra.schedule;

import com.cowave.commons.client.http.constants.HttpCode;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.tools.Collections;
import com.cowave.commons.tools.EnumVal;
import com.cowave.sys.job.domain.*;
import com.cowave.sys.job.domain.enums.JobBlockStrategy;
import com.cowave.sys.job.domain.enums.JobTaskType;
import com.cowave.sys.job.domain.enums.JobTriggerStatus;
import com.cowave.sys.job.domain.client.TriggerRequest;
import com.cowave.sys.job.domain.enums.JobTriggerType;
import com.cowave.sys.job.infra.dao.JobClientDao;
import com.cowave.sys.job.infra.dao.JobClientHandlerDao;
import com.cowave.sys.job.infra.dao.JobTriggerLogDao;
import com.cowave.sys.job.infra.dao.JobTriggerDao;
import com.cowave.sys.job.infra.ClientService;
import com.cowave.sys.job.infra.route.JobRouteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cowave.sys.job.domain.enums.JobBlockStrategy.SERIAL_EXECUTION;
import static com.cowave.sys.job.infra.route.JobRouteStrategy.SHARDING_BROADCAST;

/**
 * @author xuxueli/shanhuiming
 */
@Slf4j
@Component
public class ScheduleTrigger {

    @Resource
    private ClientService clientService;

    @Resource
    private JobClientDao jobClientDao;

    @Resource
    private JobTriggerDao jobTriggerDao;

    @Resource
    private JobTriggerLogDao jobTriggerLogDao;

    @Resource
    private JobClientHandlerDao jobClientHandlerDao;

    public void trigger(int jobId, JobTriggerType triggerType, String executorShardingParam, String executorParam) {
        JobTrigger jobTrigger = jobTriggerDao.getById(jobId);
        if (jobTrigger == null) {
            return;
        }

        jobTrigger.setHandlerParam(executorParam);

        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam != null) {
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length == 2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.parseInt(shardingArr[0]);
                shardingParam[1] = Integer.parseInt(shardingArr[1]);
            }
        }

        String handler = jobTrigger.getHandlerName();
        // 找到client列表
        List<JobClient> clientList;
        if (JobTaskType.BEAN.name().equals(jobTrigger.getTaskType())) {
            List<Integer> clientIdList = jobClientHandlerDao.listClientByHandler(handler);
            if (clientIdList.isEmpty()) {
                log.warn("no client exist for handler[{}]", handler);
                return;
            }
            clientList = jobClientDao.listAvailableClients(clientIdList);
        } else {
            clientList = jobClientDao.listAvailableClients();
        }

        if (clientList.isEmpty()) {
            log.warn("no client available for handler[{}]", handler);
            return;
        }

        if (SHARDING_BROADCAST.equalsName(jobTrigger.getRouteStrategy())
                && !clientList.isEmpty() && shardingParam == null) {
            for (int i = 0; i < clientList.size(); i++) {
                processTrigger(clientList, jobTrigger, triggerType, i, clientList.size());
            }
        } else {
            if (shardingParam == null) {
                shardingParam = new int[]{0, 1};
            }
            processTrigger(clientList, jobTrigger, triggerType, shardingParam[0], shardingParam[1]);
        }
    }

    private void processTrigger(List<JobClient> clientList, JobTrigger jobTrigger, JobTriggerType triggerType, int index, int total) {
        JobBlockStrategy blockStrategy = EnumVal.getInstance(JobBlockStrategy.class, jobTrigger.getBlockStrategy());
        if(blockStrategy == null){
            blockStrategy = SERIAL_EXECUTION;
        }
        String shardingParam = SHARDING_BROADCAST.equalsName(jobTrigger.getRouteStrategy()) ? String.valueOf(index).concat("/").concat(String.valueOf(total)) : null;

        // log
        JobTriggerLog triggerLog = new JobTriggerLog();
        triggerLog.setTriggerId(jobTrigger.getId());
        triggerLog.setTaskName(jobTrigger.getTaskName());
        triggerLog.setTriggerType(triggerType.name());
        triggerLog.setTriggerTime(new Date());
        triggerLog.setHandlerName(jobTrigger.getHandlerName());
        triggerLog.setHandlerParam(jobTrigger.getHandlerParam());
        triggerLog.setShardingParam(shardingParam);
        jobTriggerLogDao.save(triggerLog);
        jobTriggerDao.increaseTriggerTimes(jobTrigger.getId());

        // 2、trigger-param
        TriggerRequest triggerRequest = new TriggerRequest();
        triggerRequest.setTriggerId(jobTrigger.getId());
        triggerRequest.setLogId(triggerLog.getId());
        triggerRequest.setHandlerName(jobTrigger.getHandlerName());
        triggerRequest.setHandlerParams(jobTrigger.getHandlerParam());

        triggerRequest.setBlockStrategy(blockStrategy.name());
        triggerRequest.setTimeOut(jobTrigger.getTaskOverTime());
        triggerRequest.setGlueType(jobTrigger.getTaskType());
        triggerRequest.setGlueSource(jobTrigger.getGlueSource());
        triggerRequest.setGlueUpdateTime(jobTrigger.getGlueUpdateTime());
        triggerRequest.setBroadIndex(index);
        triggerRequest.setBroadTotal(total);

        JobRouteStrategy routeStrategy = EnumVal.getInstance(JobRouteStrategy.class, jobTrigger.getRouteStrategy());
        // 路由地址
        String address = null;
        if (!clientList.isEmpty()) {
            List<String> addressList = Collections.copyToList(clientList, JobClient::getClientAddress);
            if (SHARDING_BROADCAST == routeStrategy) {
                if (index < clientList.size()) {
                    address = clientList.get(index).getClientAddress();
                } else {
                    address = clientList.get(0).getClientAddress();
                }
            } else {
                address = routeStrategy.getRouter().route(clientService, triggerRequest, addressList);
            }
        }

        // 路由失败
        if (address == null) {
            triggerLog.setTriggerStatus(JobTriggerStatus.ROUTE_FAIL.getStatus());
            jobTriggerLogDao.updateById(triggerLog);
            return;
        }
        triggerLog.setClientAddress(address);

        // 执行
        try {
            Response<String> response = clientService.exec(address, triggerRequest);
            if (Objects.equals(response.getCode(), HttpCode.SUCCESS.getCode())) {
                triggerLog.setTriggerStatus(JobTriggerStatus.REMOTE_SUCCESS.getStatus());
            } else {
                triggerLog.setTriggerStatus(JobTriggerStatus.REMOTE_FAIL.getStatus());
                triggerLog.setFailMsg(response.getMsg());
            }
        } catch (Exception e) {
            triggerLog.setTriggerStatus(JobTriggerStatus.HTTP_FAIL.getStatus());
            triggerLog.setFailMsg(e.getMessage());
        }
        jobTriggerLogDao.updateById(triggerLog);
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
