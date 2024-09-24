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
 * 资源状态
 *
 * @author shanhuiming
 */
public enum PoolStatus implements DictDoc {

    /**
     * 空闲
     */
    IDLE(0, "空闲"),

    /**
     * 已占用
     */
    OCCUPY(10, "已占用"),

    /**
     * 已回收
     */
    RECYCLE(100, "已回收"),

    /**
     * 强制回收
     */
    RECYCLE_FORCE(110, "强制回收"),

    /**
     * 已废弃
     */
    DEPRECATE(150, "已废弃");

    private final int code;

    private final String desc;

    PoolStatus(int code, String desc) {
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
