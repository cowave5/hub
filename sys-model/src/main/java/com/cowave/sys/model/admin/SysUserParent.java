/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import lombok.Data;

/**
 * 用户关系
 *
 * @author shanhuiming
 *
 */
@Data
public class SysUserParent {

    /**
     * 关系类型
     */
    private Integer treeType;

    /**
     * 上级用户id
     */
    private Long parentId;

    /**
     * 上级用户编码
     */
    private String parentCode;

    /**
     * 上级用户名称
     */
    private String parentName;
}
