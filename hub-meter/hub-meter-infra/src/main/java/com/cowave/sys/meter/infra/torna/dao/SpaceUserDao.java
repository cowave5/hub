package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.SpaceUser;
import com.cowave.sys.meter.infra.torna.dao.mapper.SpaceUserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpaceUserDao extends ServiceImpl<SpaceUserMapper, SpaceUser> {

}
