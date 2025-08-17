package com.cowave.sys.meter.infra.api.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.bean.Booleans;
import com.cowave.sys.meter.domain.api.ApiDefinition;
import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.infra.api.dao.mapper.ApiDefinitionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiDefinitionDao extends ServiceImpl<ApiDefinitionMapper, ApiDefinition> {

    /**
     * 获取Module文档
     */
    public List<ApiDefinition> listByFolderId(Long folderId) {
        return lambdaQuery().eq(ApiDefinition::getFolderId, folderId).list();
    }

    public ApiDefinition getByDocId(Long docId) {
        return lambdaQuery()
                .eq(ApiDefinition::getId, docId)
                .eq(ApiDefinition::getIsShow, Booleans.TRUE)
                .one();
    }

    public List<ApiDefinition> listByDocIds(List<Long> docIds) {
        return lambdaQuery()
                .in(ApiDefinition::getId, docIds)
                .eq(ApiDefinition::getIsShow, Booleans.TRUE)
                .list();
    }


    public List<Long> listDocIdByModuleId(Long moduleId) {
        return lambdaQuery()
                .eq(ApiDefinition::getFolderId, moduleId)
                .select(ApiDefinition::getFolderId)
                .list().stream().map(ApiDefinition::getId).toList();
    }

    public void deleteOpenAPIModuleDocs(long moduleId) {
        lambdaUpdate()
                .eq(ApiDefinition::getFolderId, moduleId)
                .eq(ApiDefinition::getCreateMode, OperationMode.OPEN.getType())
                .eq(ApiDefinition::getIsLocked, Booleans.FALSE)
                .and(wrapper -> wrapper
                        .isNull(ApiDefinition::getVersion)
                        .or()
                        .eq(ApiDefinition::getVersion, "")
                ).remove();
    }
}
