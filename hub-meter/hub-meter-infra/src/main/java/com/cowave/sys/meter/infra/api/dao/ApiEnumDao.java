package com.cowave.sys.meter.infra.api.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.api.ApiEnum;
import com.cowave.sys.meter.infra.api.dao.mapper.ApiEnumMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ApiEnumDao extends ServiceImpl<ApiEnumMapper, ApiEnum> {

    public ApiEnum getByDataId(String dataId) {
        return lambdaQuery().eq(ApiEnum::getDataId, dataId).one();
    }
}
