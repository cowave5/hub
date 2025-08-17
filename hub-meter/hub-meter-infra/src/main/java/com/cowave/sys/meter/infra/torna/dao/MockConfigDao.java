package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.MockConfig;
import com.cowave.sys.meter.infra.torna.dao.mapper.MockConfigMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MockConfigDao extends ServiceImpl<MockConfigMapper, MockConfig> {

}
