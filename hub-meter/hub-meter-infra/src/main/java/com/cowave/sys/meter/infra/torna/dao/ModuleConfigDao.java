package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ModuleConfig;
import com.cowave.sys.meter.domain.torna.enums.ModuleConfigTypeEnum;
import com.cowave.sys.meter.infra.torna.dao.mapper.ModuleConfigMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ModuleConfigDao extends ServiceImpl<ModuleConfigMapper, ModuleConfig> {

    public String getCommonConfigValue(long moduleId, String key, String defaultValue) {
        ModuleConfig commonConfig = getCommonConfig(moduleId, key);
        return Optional.ofNullable(commonConfig)
                .map(ModuleConfig::getConfigValue)
                .orElse(defaultValue);
    }

    public ModuleConfig getCommonConfig(long moduleId, String key) {
        return getModuleConfig(moduleId, key, ModuleConfigTypeEnum.COMMON, false);
    }

    public ModuleConfig getModuleConfig(long moduleId, String key, ModuleConfigTypeEnum type, boolean forceQuery) {
        return lambdaQuery()
                .eq(ModuleConfig::getModuleId, moduleId)
                .eq(ModuleConfig::getType, type.getType())
                .eq(ModuleConfig::getConfigKey, key)
                .one();
    }
}
