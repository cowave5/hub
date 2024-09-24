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

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class TreeNode {

    /**
     * id
     */
    private String id;

    /**
     * 上级id
     */
    private String pid;

    /**
     * 名称
     */
    private String label;

    /**
     * 内容
     */
    private String content;
}
