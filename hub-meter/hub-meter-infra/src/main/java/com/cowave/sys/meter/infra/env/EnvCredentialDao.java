package com.cowave.sys.meter.infra.env;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.meter.domain.env.EnvCredential;
import com.cowave.sys.meter.domain.env.request.CredentialQuery;
import com.cowave.sys.meter.infra.env.mapper.EnvCredentialMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class EnvCredentialDao extends ServiceImpl<EnvCredentialMapper, EnvCredential> {

    /**
     * 分页列表
     */
    public Page<EnvCredential> pageList(CredentialQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getCredentialName()), EnvCredential::getCredentialName, query.getCredentialName())
                .ge(query.getBeginTime() != null, EnvCredential::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, EnvCredential::getCreateTime, query.getEndTime())
                .page(Access.page());
    }
}
