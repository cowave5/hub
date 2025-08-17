package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.ShareEnvironment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：share_environment
 * 备注：分享模块调试环境关联表
 *
 * @author Joker
 */
@Mapper
public interface ShareEnvironmentMapper extends BaseMapper<ShareEnvironment> {

}
