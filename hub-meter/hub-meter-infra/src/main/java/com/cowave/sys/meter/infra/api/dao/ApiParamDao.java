package com.cowave.sys.meter.infra.api.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.api.ApiParam;
import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.infra.api.dao.mapper.ApiParamMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiParamDao extends ServiceImpl<ApiParamMapper, ApiParam> {

    public void deletePushParam(List<Long> docIdList) {
        lambdaUpdate()
                .in(ApiParam::getApiId, docIdList)
                .eq(ApiParam::getCreateMode, OperationMode.OPEN.getType())
                .remove();
    }

    public ApiParam getByDataId(String dataId) {
        return lambdaQuery().eq(ApiParam::getDataId, dataId).one();
    }

    public List<ApiParam> listByDocId(Long docId) {
        return lambdaQuery().eq(ApiParam::getApiId, docId).list();
    }
}
