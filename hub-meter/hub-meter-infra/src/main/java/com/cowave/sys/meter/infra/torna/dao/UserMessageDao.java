package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.UserMessage;
import com.cowave.sys.meter.infra.torna.dao.mapper.UserMessageMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserMessageDao extends ServiceImpl<UserMessageMapper, UserMessage> {

}
