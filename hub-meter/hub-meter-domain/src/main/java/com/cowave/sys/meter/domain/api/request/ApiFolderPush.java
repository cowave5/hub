package com.cowave.sys.meter.domain.api.request;

import com.cowave.sys.meter.domain.torna.param.DebugEnvParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author tanghc
 */
@Data
public class ApiFolderPush {

    /**
     * 调试环境
     */
    private List<DebugEnvParam> debugEnvs;

    /**
     * api列表
     */
    @NotEmpty(message = "文档内容不能为空")
    private List<ApiPush> apis;

    /**
     * 推送人
     */
    private String author;

    /**
     * 公共错误码
     */
    private List<ApiCodePush> commonErrorCodes;

    /**
     * 是否替换文档，1：替换，0：不替换
     */
    private Byte isReplace = 1;

    /**
     * 是否覆盖文档，1：覆盖，0：不覆盖
     */
    private Byte isOverride = 0;
}
