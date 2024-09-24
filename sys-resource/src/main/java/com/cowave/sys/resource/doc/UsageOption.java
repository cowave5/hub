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
 * usage调整动作
 *
 * @author shanhuiming
 */
public enum UsageOption implements DictDoc {

    /**
     * 不变
     */
    REMAINED(0, "不变"),

    /**
     * 新增
     */
    ADD(1, "新增"),

    /**
     * 更新
     */
    UPDATE(2, "更新"),

    /**
     * 删除
     */
    DELETE(3, "删除");

    private final int code;

    private final String desc;

    UsageOption(int code, String desc) {
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
