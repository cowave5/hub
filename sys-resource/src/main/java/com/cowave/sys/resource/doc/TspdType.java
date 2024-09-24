/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.doc;

import com.cowave.commons.framework.helper.dict.DictDoc;

/**
 * 转发器类型
 *
 * @author shanhuiming
 */
public enum TspdType implements DictDoc {

    /**
     * 透明转发器
     */
    TRANSPARENT(1, "透明转发器"),

    /**
     * KU转发器
     */
    KU(2, "KU转发器");

    private final int code;

    private final String desc;

    TspdType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
