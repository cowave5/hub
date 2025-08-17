package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.api.ApiCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：constant_info
 * 备注：常量信息
 *
 * @author tanghc
 */
@Mapper
public interface ConstantInfoMapper extends BaseMapper<ApiCode> {

}
