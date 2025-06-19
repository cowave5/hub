package com.cowave.sys.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.job.domain.JobTriggerLog;
import com.cowave.sys.job.domain.request.JobTriggerLogQuery;

/**
 * @author xuxueli/shanhuiming
 */
public interface JobTriggerLogService {

    Page<JobTriggerLog> pageList(JobTriggerLogQuery query);
}
