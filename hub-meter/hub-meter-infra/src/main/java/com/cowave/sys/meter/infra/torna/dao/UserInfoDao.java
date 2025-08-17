package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.UserInfo;
import com.cowave.sys.meter.infra.torna.dao.mapper.UserInfoMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoDao extends ServiceImpl<UserInfoMapper, UserInfo> {

}
