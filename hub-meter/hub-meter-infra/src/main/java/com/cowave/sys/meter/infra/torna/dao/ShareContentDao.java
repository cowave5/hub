package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ShareContent;
import com.cowave.sys.meter.infra.torna.dao.mapper.ShareContentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ShareContentDao extends ServiceImpl<ShareContentMapper, ShareContent> {

}
