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
public class RoleAuthed {

    /**
     * 菜单id
     */
    @NotNull(message = "menu.notnull.id")
    private Long menuId;

    /**
     * 角色id列表
     */
    private List<Long> roleIds;

    /**
     * 角色名称
     */
    private String roleName;
}
