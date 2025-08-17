package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Project;
import com.cowave.sys.meter.infra.torna.dao.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao extends ServiceImpl<ProjectMapper, Project> {

}
