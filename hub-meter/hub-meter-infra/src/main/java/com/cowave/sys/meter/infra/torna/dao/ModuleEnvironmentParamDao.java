package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ModuleEnvironmentParam;
import com.cowave.sys.meter.infra.torna.dao.mapper.ModuleEnvironmentParamMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ModuleEnvironmentParamDao extends ServiceImpl<ModuleEnvironmentParamMapper, ModuleEnvironmentParam> {

    public List<ModuleEnvironmentParam> listByEnvironmentAndStyle(Long environmentId, byte style) {
        if (environmentId == null) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(ModuleEnvironmentParam::getEnvironmentId, environmentId)
                .eq(ModuleEnvironmentParam::getStyle, style)
                .orderByAsc(ModuleEnvironmentParam::getOrderIndex)
                .orderByAsc(ModuleEnvironmentParam::getId)
                .list();
    }
}
