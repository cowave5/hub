/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class UserAuthed {

    /**
     * 角色id
     */
    @NotNull(message = "role.notnull.id")
    private Long roleId;

    /**
     * 用户id列表
     */
    private List<Long> userIds;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 岗位id
     */
    private Long postId;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;
}
