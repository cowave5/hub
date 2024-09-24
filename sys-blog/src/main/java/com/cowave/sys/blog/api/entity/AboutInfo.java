/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.entity;

import lombok.Data;

/**
 * About信息
 */
@Data
public class AboutInfo {

    /**
     * id
     */
    private String id;

    /**
     * About头像
     */
    private String abAvatar;

    /**
     * About名称
     */
    private String abName;

    /**
     * About描述
     */
    private String abText;

}
