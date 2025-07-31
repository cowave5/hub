package com.cowave.sys.meter.infra.build;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.build.BuildHistory;
import com.cowave.sys.meter.infra.build.mapper.BuildHistoryMapper;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class BuildHistoryDao extends ServiceImpl<BuildHistoryMapper, BuildHistory> {

}
