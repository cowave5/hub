package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.job.domain.JobTriggerLog;
import com.cowave.sys.job.domain.client.TriggerResponse;
import com.cowave.sys.job.domain.request.JobTriggerLogQuery;
import com.cowave.sys.job.infra.dao.mapper.JobTriggerLogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobTriggerLogDao extends ServiceImpl<JobTriggerLogMapper, JobTriggerLog> {

    /**
     * 日志分页查询
     */
    public Page<JobTriggerLog> pageList(JobTriggerLogQuery query) {
        return lambdaQuery().orderByDesc(JobTriggerLog::getTriggerTime).page(Access.page());
    }

    /**
     * 删除任务日志
     */
    public void removeByTriggerIds(List<Integer> jobIds){
        lambdaUpdate().in(JobTriggerLog::getTriggerId, jobIds).remove();
    }

    /**
     * 任务回调更新日志
     */
    public void completeTriggerLog(TriggerResponse triggerResponse) {
        lambdaUpdate()
                .eq(JobTriggerLog::getTriggerId, triggerResponse.getTriggerId())
                .eq(JobTriggerLog::getId, triggerResponse.getLogId())
                .set(JobTriggerLog::getTriggerStatus, triggerResponse.getHandleStatus())
                .set(JobTriggerLog::getHandleTime, triggerResponse.getHandleTime())
                .set(JobTriggerLog::getHandleCost, triggerResponse.getHandleCost())
                .set(JobTriggerLog::getFailMsg, triggerResponse.getHandleMsg())
                .update();
    }
}
