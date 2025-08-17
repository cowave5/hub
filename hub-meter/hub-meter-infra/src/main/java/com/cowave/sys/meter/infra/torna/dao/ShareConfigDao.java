package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ShareConfig;
import com.cowave.sys.meter.infra.torna.dao.mapper.ShareConfigMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ShareConfigDao extends ServiceImpl<ShareConfigMapper, ShareConfig> {

}
