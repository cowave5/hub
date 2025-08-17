package com.cowave.sys.meter.domain.torna.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.sys.meter.domain.torna.bean.Booleans;
import com.cowave.sys.meter.domain.torna.bean.DocInfoDataId;
import com.cowave.sys.meter.domain.torna.bean.User;
import com.cowave.sys.meter.domain.torna.enums.DocTypeEnum;
import com.cowave.sys.meter.domain.torna.support.IdCodec;
import lombok.Data;

import java.util.Map;

/**
 * @author tanghc
 */
@Data
public class DocFolderCreateDTO implements DocInfoDataId {
    @JSONField(serializeUsing = IdCodec.class, deserializeUsing = IdCodec.class)
    private Long folderId;

    private String name;

    @JSONField(serializeUsing = IdCodec.class, deserializeUsing = IdCodec.class)
    private Long parentId;

    /** 维护人, 数据库字段：author */
    private String author;

    private Integer orderIndex;

    private DocTypeEnum docTypeEnum;

    private Map<String, ?> props;

    private User user;

    @Override
    public Byte getIsGroup() {
        return Booleans.TRUE;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getHttpMethod() {
        return "";
    }
}
