/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Data
@AllArgsConstructor
public class TypeField {

    public static final List<TypeField> TYPE_INTERNAL = new ArrayList<>();

    static{
        TYPE_INTERNAL.add(new TypeField(-1L, "String"));
        TYPE_INTERNAL.add(new TypeField(-2L, "Integer"));
        TYPE_INTERNAL.add(new TypeField(-3L, "Long"));
        TYPE_INTERNAL.add(new TypeField(-4L, "Float"));
        TYPE_INTERNAL.add(new TypeField(-5L, "Double"));
        TYPE_INTERNAL.add(new TypeField(-6L, "BigDecimal"));
        TYPE_INTERNAL.add(new TypeField(-7L, "Date"));
        TYPE_INTERNAL.add(new TypeField(-8L, "Byte[]"));
        TYPE_INTERNAL.add(new TypeField(-9L, "Map<String, Object>"));
    }

    private Long key;

    private String label;
}
