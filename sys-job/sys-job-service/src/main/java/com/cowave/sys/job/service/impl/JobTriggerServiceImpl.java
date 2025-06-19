package com.cowave.sys.job.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.domain.request.JobTriggerQuery;
import com.cowave.sys.job.infra.dao.JobTriggerDao;
import com.cowave.sys.job.infra.dao.JobTriggerLogDao;
import com.cowave.sys.job.infra.schedule.ScheduleTrigger;
import com.cowave.sys.job.service.JobTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.sys.job.infra.schedule.TriggerTypeEnum.MANUAL;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class JobTriggerServiceImpl implements JobTriggerService {
    private final ScheduleTrigger scheduleTrigger;
    private final JobTriggerDao jobTriggerDao;
    private final JobTriggerLogDao jobTriggerLogDao;

    @Override
    public Page<JobTrigger> pageList(JobTriggerQuery query) {
        return jobTriggerDao.pageList(query);
    }

    @Override
    public void create(JobTrigger jobTrigger) {
        jobTrigger.setCreateBy(Access.userAccount());
        jobTriggerDao.save(jobTrigger);
    }

    @Override
    public void delete(List<Integer> jobIds) {
        jobTriggerDao.removeByIds(jobIds);
        jobTriggerLogDao.removeByTriggerIds(jobIds);
    }

    @Override
    public void exec(Integer id) {
        scheduleTrigger.trigger(id, MANUAL, null, null);
    }

    @Override
    public void switchStatus(Integer id, Integer status) {
        jobTriggerDao.switchStatus(id, status);
    }
}
