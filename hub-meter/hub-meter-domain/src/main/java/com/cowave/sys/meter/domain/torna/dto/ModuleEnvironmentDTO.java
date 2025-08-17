package com.cowave.sys.meter.domain.torna.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.sys.meter.domain.torna.support.IdCodec;
import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class ModuleEnvironmentDTO {

    @JSONField(serializeUsing = IdCodec.class, deserializeUsing = IdCodec.class)
    private Long id;

    /**
     * module.id
     */
    @JSONField(serializeUsing = IdCodec.class, deserializeUsing = IdCodec.class)
    private Long moduleId;

    /**
     * 环境名称
     */
    private String name;

    /**
     * 调试路径
     */
    private String url;

    /**
     * 是否公开
     */
    private Byte isPublic;


}
