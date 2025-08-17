package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ProjectUser;
import com.cowave.sys.meter.infra.torna.dao.mapper.ProjectUserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectUserDao extends ServiceImpl<ProjectUserMapper, ProjectUser> {

}
