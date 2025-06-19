package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.job.domain.JobTriggerGlue;
import com.cowave.sys.job.infra.dao.mapper.JobGlueMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobGlueDao extends ServiceImpl<JobGlueMapper, JobTriggerGlue> {

}
