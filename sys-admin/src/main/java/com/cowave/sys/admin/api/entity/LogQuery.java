/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import lombok.Data;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class LogQuery {

    private Long parentId;

    private List<Long> roleIds;

    private List<Long> parentIds;

    private List<String> deptPostIds;

    private String parent;

    private List<String> roles;

    private List<String> posts;

    private List<String> parents;
}
