/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.entity;

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
