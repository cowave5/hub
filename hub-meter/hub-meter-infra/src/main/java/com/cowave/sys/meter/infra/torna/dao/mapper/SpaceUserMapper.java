package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.SpaceUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghc
 */
@Mapper
public interface SpaceUserMapper extends BaseMapper<SpaceUser> {

//    int insertBatch(@Param("items") List<SpaceUser> items);

}
