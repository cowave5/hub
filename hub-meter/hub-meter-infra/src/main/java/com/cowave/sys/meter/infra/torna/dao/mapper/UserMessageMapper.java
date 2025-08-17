package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tanghc
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

//    @Update("UPDATE user_message SET is_read=1 WHERE user_id=#{userId} AND is_read=0")
//    int readAll(@Param("userId") long userId);

}
