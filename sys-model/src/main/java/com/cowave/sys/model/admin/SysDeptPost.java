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

import javax.validation.constraints.NotNull;

/**
 * 部门岗位
 *
 * @author shanhuiming
 *
 */
@Data
public class SysDeptPost {

    /**
     * 部门id
     */
    @NotNull(message = "{dept.notnull.id}")
    private Long deptId;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位id
     */
    @NotNull(message = "{post.notnull.id}")
    private Long postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 是否部门默认岗位
     */
    private Integer isDefault = 0;
}
