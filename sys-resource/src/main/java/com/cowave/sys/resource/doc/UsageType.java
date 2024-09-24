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
 * 使用类型
 *
 * @author shanhuiming
 */
public enum UsageType implements DictDoc {

    /**
     * 事务申请
     */
    DUTY(1, "事务申请"),

    /**
     * 任务组网
     */
    TASK(2, "任务组网"),

    /**
     * 任务申请
     */
    TASK_DUTY(3, "任务申请"),

    /**
     * 预分配
     */
    DISTRIBUTE(4, "预分配"),

    /**
     * 资源预留
     */
    RESERVE(5, "资源预留");

    private final int code;

    private final String desc;

    UsageType(int code, String desc) {
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
