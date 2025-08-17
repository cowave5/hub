package com.cowave.sys.meter.domain.torna.bean;

import com.cowave.sys.meter.domain.torna.util.DataIdUtil;

/**
 * @author tanghc
 */
public interface EnumInfoDataId {

    /**
     * 唯一id，md5(module_id:name)
     *
     * @return
     */
    default String buildDataId() {
        return DataIdUtil.getEnumInfoDataId(getFolderId(), getName());
    }

    Long getFolderId();

    String getName();

}
