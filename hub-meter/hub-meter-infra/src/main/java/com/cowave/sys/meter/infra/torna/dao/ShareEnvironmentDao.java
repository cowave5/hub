package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ShareEnvironment;
import com.cowave.sys.meter.infra.torna.dao.mapper.ShareEnvironmentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ShareEnvironmentDao extends ServiceImpl<ShareEnvironmentMapper, ShareEnvironment> {

}
