/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.entity;

import lombok.Data;

/**
 * 序列信息
 *
 * @author shanhuiming
 */
@Data
public class DbSequence {

    /**
     * id
     */
    private Long id;

    /**
     * 数据库id
     */
    private Long dbId;

    /**
     * 序列名称
     */
    private String sequenceName;

    /**
     * 最小值
     */
    private Long minValue;

    /**
     * 最大值
     */
    private Long maxValue;

    /**
     * 步长
     */
    private Long incrementBy;

    /**
     * 最新值
     */
    private Long lastNumber;
}
