package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.job.domain.JobClient;
import com.cowave.sys.job.infra.dao.mapper.JobClientMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobClientDao extends ServiceImpl<JobClientMapper, JobClient> {

    public Integer registry(JobClient jobClient) {
        JobClient exist = lambdaQuery()
                .eq(JobClient::getClientName, jobClient.getClientName())
                .eq(JobClient::getClientAddress, jobClient.getClientAddress())
                .one();
        if (exist == null) {
            this.save(jobClient);
            return jobClient.getId();
        } else {
            exist.setUpdateTime(new Date());
            this.updateById(exist);
            return exist.getId();
        }
    }

    public Integer unRegistry(JobClient jobClient) {
        JobClient exist = lambdaQuery()
                .eq(JobClient::getClientName, jobClient.getClientName())
                .eq(JobClient::getClientAddress, jobClient.getClientAddress())
                .one();
        if(exist != null){
            removeById(exist);
            return exist.getId();
        }
        return null;
    }

    public List<JobClient> listAvailableClients(){
        // 5min内有心跳
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        return lambdaQuery().ge(JobClient::getUpdateTime, fiveMinutesAgo).list();
    }

    public List<JobClient> listAvailableClients(List<Integer> clientIds){
        // 5min内有心跳
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        return lambdaQuery()
                .in(JobClient::getId, clientIds)
                .ge(JobClient::getUpdateTime, fiveMinutesAgo)
                .list();
    }
}
