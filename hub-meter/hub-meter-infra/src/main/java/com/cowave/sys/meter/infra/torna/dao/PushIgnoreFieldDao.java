package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.PushIgnoreField;
import com.cowave.sys.meter.infra.torna.dao.mapper.PushIgnoreFieldMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PushIgnoreFieldDao extends ServiceImpl<PushIgnoreFieldMapper, PushIgnoreField> {

    public boolean isPushIgnore(long moduleId, String dataId, String fieldName) {
        return getByDataIdAndFieldName(moduleId, dataId, fieldName) != null;
    }

    public PushIgnoreField getByDataIdAndFieldName(long moduleId, String dataId, String fieldName) {
        return lambdaQuery()
                .eq(PushIgnoreField::getModuleId, moduleId)
                .eq(PushIgnoreField::getDataId, dataId)
                .eq(PushIgnoreField::getFieldName, fieldName)
                .one();
    }
}
