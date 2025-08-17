package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.bean.Booleans;
import com.cowave.sys.meter.domain.torna.dto.ModuleEnvironmentDTO;
import com.cowave.sys.meter.domain.torna.entity.ModuleEnvironment;
import com.cowave.sys.meter.infra.torna.dao.mapper.ModuleEnvironmentMapper;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
public class ModuleEnvironmentDao extends ServiceImpl<ModuleEnvironmentMapper, ModuleEnvironment> {

    public List<ModuleEnvironment> listModuleEnvironment(long moduleId) {
        List<ModuleEnvironment> list = lambdaQuery().eq(ModuleEnvironment::getModuleId, moduleId).list();
        list.sort(Comparator.comparing(ModuleEnvironment::getGmtCreate));
        return list;
    }

    public ModuleEnvironment getFirst(long moduleId) {
        return lambdaQuery()
                .eq(ModuleEnvironment::getModuleId, moduleId)
                .orderByAsc(ModuleEnvironment::getId)
                .last("LIMIT 1")
                .one();
    }

    public ModuleEnvironment getByModuleIdAndName(long moduleId, String name) {
        return lambdaQuery()
                .eq(ModuleEnvironment::getModuleId, moduleId)
                .eq(ModuleEnvironment::getName, name)
                .one();
    }

    public void setDebugEnv(long moduleId, String name, String url) {
        this.setDebugEnv(moduleId, name, url, false);
    }

    public ModuleEnvironment setDebugEnv(long moduleId, String name, String url, boolean isPublic) {
        ModuleEnvironmentDTO moduleEnvironmentSettingDTO = new ModuleEnvironmentDTO();
        moduleEnvironmentSettingDTO.setModuleId(moduleId);
        moduleEnvironmentSettingDTO.setName(name);
        moduleEnvironmentSettingDTO.setUrl(url);
        moduleEnvironmentSettingDTO.setIsPublic(Booleans.toValue(isPublic));
        return setEnvironment(moduleEnvironmentSettingDTO);
    }

    public ModuleEnvironment setEnvironment(ModuleEnvironmentDTO moduleEnvironmentSettingDTO) {
        Long moduleId = moduleEnvironmentSettingDTO.getModuleId();
        String name = moduleEnvironmentSettingDTO.getName();
        String url = moduleEnvironmentSettingDTO.getUrl();
        Byte isPublic = moduleEnvironmentSettingDTO.getIsPublic();
        ModuleEnvironment moduleEnvironment = getByModuleIdAndName(moduleId, name);
        if (moduleEnvironment == null) {
            moduleEnvironment = new ModuleEnvironment();
            moduleEnvironment.setModuleId(moduleId);
            moduleEnvironment.setName(name);
            moduleEnvironment.setUrl(url);
            moduleEnvironment.setIsPublic(isPublic);
            save(moduleEnvironment);
        } else {
            moduleEnvironment.setUrl(url);
            moduleEnvironment.setIsPublic(isPublic);
            updateById(moduleEnvironment);
        }
        return moduleEnvironment;
    }
}
