package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.domain.request.JobTriggerQuery;
import com.cowave.sys.job.infra.dao.mapper.JobTriggerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobTriggerDao extends ServiceImpl<JobTriggerMapper, JobTrigger> {

    /**
     * 分页查询
     */
    public Page<JobTrigger> pageList(JobTriggerQuery query){
        return lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getTaskType()), JobTrigger::getTaskType, query.getTaskType())
                .like(StringUtils.isNotBlank(query.getTaskName()), JobTrigger::getTaskName, query.getTaskName())
                .ge(query.getTaskBeginTime() != null, JobTrigger::getTaskBeginTime, query.getTaskBeginTime())
                .le(query.getTaskEndTime() != null, JobTrigger::getTaskEndTime, query.getTaskEndTime())
                .orderByDesc(JobTrigger::getCreateTime)
                .page(Access.page());
    }

    /**
     * 切换状态
     */
    public void switchStatus(Integer id, Integer status) {
        lambdaUpdate().eq(JobTrigger::getId, id).set(JobTrigger::getStatus, status).update();
    }

    /**
     * schedule
     */
    public List<JobTrigger> preListInSeconds(long ms, int limit){
        return lambdaQuery()
                .eq(JobTrigger::getStatus, 1)
                .le(JobTrigger::getTriggerNextTime, ms)
                .last("limit " + limit)
                .list();
    }

    /**
     * schedule
     */
    public void updateTriggerTime(JobTrigger jobTrigger){
        lambdaUpdate()
                .eq(JobTrigger::getId, jobTrigger.getId())
                .eq(JobTrigger::getStatus, 1)
                .set(JobTrigger::getTriggerLastTime, jobTrigger.getTriggerLastTime())
                .set(JobTrigger::getTriggerNextTime, jobTrigger.getTriggerNextTime())
                .update();
    }

    /**
     * schedule
     */
    public void increaseTriggerTimes(int triggerId){
        lambdaUpdate()
                .eq(JobTrigger::getId, triggerId)
                .setSql("trigger_times = trigger_times + 1")
                .update();
    }

    /**
     * schedule
     */
    public void increaseTriggerSuccessTimes(int triggerId){
        lambdaUpdate()
                .eq(JobTrigger::getId, triggerId)
                .setSql("trigger_success_times = trigger_success_times + 1")
                .update();
    }
}
