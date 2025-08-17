package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.UserSubscribe;
import com.cowave.sys.meter.infra.torna.dao.mapper.UserSubscribeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserSubscribeDao extends ServiceImpl<UserSubscribeMapper, UserSubscribe> {

}
