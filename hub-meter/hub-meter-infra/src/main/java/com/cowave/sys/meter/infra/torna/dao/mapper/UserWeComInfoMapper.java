package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.UserWeComInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：user_wecom_info
 * 备注：企业微信开放平台用户
 *
 * @author Lin
 * @date 2023-11-29  17:03:14
 */
@Mapper
public interface UserWeComInfoMapper extends BaseMapper<UserWeComInfo> {

}
