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
 * 资源类型
 *
 * @author shanhuiming
 */
public enum PoolType implements DictDoc {

    /**
     * 创建
     */
    T0(0, "创建"),

    /**
     * 预分配
     */
    T1(1, "预分配"),

    /**
     * 事务申请
     */
    T2(5, "事务申请"),

    /**
     * 任务申请
     */
    T3(10, "任务申请");

    private final int code;

    private final String desc;

    PoolType(int code, String desc) {
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
