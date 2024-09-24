/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章专栏
 *
 * @author shanhuiming
 */
@Data
@TableName("info_channel")
public class ChannelInfo {

    /**
     * id
     */
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 总数量
     */
    private Long weight;
}
