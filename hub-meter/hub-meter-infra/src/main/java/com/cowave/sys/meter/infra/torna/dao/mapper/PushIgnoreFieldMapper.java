package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.PushIgnoreField;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：push_ignore_field
 * 备注：推送忽略字段
 *
 * @author tanghc
 */
@Mapper
public interface PushIgnoreFieldMapper extends BaseMapper<PushIgnoreField> {

}
