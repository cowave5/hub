package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.ProjectUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghc
 */
@Mapper
public interface ProjectUserMapper extends BaseMapper<ProjectUser> {

//    int insertBatch(@Param("items") List<ProjectUser> items);
//
//    int removeProjectLeader(@Param("projectId") long projectId, @Param("adminCode") String adminCode);
}
