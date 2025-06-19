package com.cowave.sys.job.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.job.domain.JobTriggerLog;
import com.cowave.sys.job.domain.request.JobTriggerLogQuery;
import com.cowave.sys.job.infra.dao.JobTriggerLogDao;
import com.cowave.sys.job.service.JobTriggerLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class JobTriggerLogServiceImpl implements JobTriggerLogService {
    private final JobTriggerLogDao jobTriggerLogDao;

    @Override
    public Page<JobTriggerLog> pageList(JobTriggerLogQuery query) {
       return jobTriggerLogDao.pageList(query);
    }
}
