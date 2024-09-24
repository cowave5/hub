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
 * 使用状态
 *
 * @author shanhuiming
 */
public enum UsageStatus implements DictDoc {

    /**
     * 资源规划
     */
    ALLOCATE(100, "资源规划"),

    /**
     * 网络规划
     */
    NETPLAN(200, "网络规划"),

    /**
     * 下发网控
     */
    NCCSEND(300, "下发网控"),

    /**
     * 调整待审批
     */
    ADJUSTING(400, "调整待审批"),

    /**
     * 撤收中
     */
    REVOKING(410, "撤收中"),

    /**
     * 已释放
     */
    RELEASE(500, "已释放");

    private final int code;

    private final String desc;

    UsageStatus(int code, String desc) {
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
