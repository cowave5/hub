package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.ModuleEnvironment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：module_environment
 * 备注：模块调试环境
 *
 * @author tanghc
 */
@Mapper
public interface ModuleEnvironmentMapper extends BaseMapper<ModuleEnvironment> {

}
