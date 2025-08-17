package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.api.ApiParam;
import com.cowave.sys.meter.domain.api.ApiCode;
import com.cowave.sys.meter.domain.torna.entity.ModuleConfig;
import com.cowave.sys.meter.domain.torna.util.HtmlTableBuilder;
import com.cowave.sys.meter.infra.torna.dao.mapper.ConstantInfoMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ConstantInfoDao extends ServiceImpl<ConstantInfoMapper, ApiCode> {

    public void setCommonErrorCodeList(List<ApiParam> docParamList, long moduleId) {
        if (CollectionUtils.isEmpty(docParamList)) {
            return;
        }
        List<ModuleConfig> moduleConfigs = docParamList.stream().map(
                docParam -> {
                    ModuleConfig config = new ModuleConfig();
                    config.setModuleId(moduleId);
                    config.setConfigKey(docParam.getName());
                    config.setConfigValue(docParam.getExample());
                    config.setDescription(docParam.getDescription());
                    return config;
                })
                .collect(Collectors.toList());
        ApiCode apiCode = this.buildConstantInfo(moduleId, moduleConfigs);
        saveModuleConstantInfo(moduleId, apiCode.getContent());
    }

    public ApiCode buildConstantInfo(long folderId, List<ModuleConfig> moduleConfigs) {
        ApiCode errorCodeInfo = new ApiCode();
        errorCodeInfo.setFolderId(folderId);
        String content = buildModuleHtmlTable(moduleConfigs);
        errorCodeInfo.setContent(content);
        return errorCodeInfo;
    }

    private String buildModuleHtmlTable(List<ModuleConfig> moduleConfigs) {
        HtmlTableBuilder htmlTableBuilder = new HtmlTableBuilder();
        htmlTableBuilder.heads("错误码", "错误描述", "解决方案");
        for (ModuleConfig moduleConfig : moduleConfigs) {
            htmlTableBuilder.addRow(
                    Arrays.asList(moduleConfig.getConfigKey(),
                            moduleConfig.getDescription(),
                            moduleConfig.getConfigValue()
                    )
            );
        }
        return htmlTableBuilder.toString();
    }

    public void saveModuleConstantInfo(long moduleId, String content) {
        ApiCode errorCodeInfo = lambdaQuery().eq(ApiCode::getFolderId, moduleId).one();
        if (errorCodeInfo == null) {
            errorCodeInfo = new ApiCode();
            errorCodeInfo.setFolderId(moduleId);
            errorCodeInfo.setContent(content);
            this.save(errorCodeInfo);
        } else {
            errorCodeInfo.setContent(content);
            this.updateById(errorCodeInfo);
        }
    }
}
