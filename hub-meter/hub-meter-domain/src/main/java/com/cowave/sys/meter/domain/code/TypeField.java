/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.domain.code;

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
