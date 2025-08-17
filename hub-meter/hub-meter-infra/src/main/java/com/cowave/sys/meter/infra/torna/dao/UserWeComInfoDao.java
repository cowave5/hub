package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.UserWeComInfo;
import com.cowave.sys.meter.infra.torna.dao.mapper.UserWeComInfoMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserWeComInfoDao extends ServiceImpl<UserWeComInfoMapper, UserWeComInfo> {

}
