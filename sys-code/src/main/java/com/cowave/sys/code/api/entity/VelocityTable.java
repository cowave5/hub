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

import java.util.List;

/**
 * 模板
 *
 * @author shanhuiming
 */
@Data
public class VelocityTable {

    private String tableName;

    private String tableComment;

    private List<DbTableColumn> columns;

    private List<DbTableIndex> indexs;
}
