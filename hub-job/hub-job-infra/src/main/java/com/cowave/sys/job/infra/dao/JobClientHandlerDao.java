package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.job.domain.JobClientHandler;
import com.cowave.sys.job.infra.dao.mapper.JobClientHandlerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobClientHandlerDao extends ServiceImpl<JobClientHandlerMapper, JobClientHandler> {

    public void removeByClientId(Integer clientId){
        lambdaUpdate().eq(JobClientHandler::getClientId, clientId).remove();
    }

    public List<Integer> listClientByHandler(String handler){
        List<JobClientHandler> list = lambdaQuery()
                .eq(JobClientHandler::getClientHandler, handler)
                .list();
        return Collections.copyToList(list, JobClientHandler::getClientId);
    }
}
