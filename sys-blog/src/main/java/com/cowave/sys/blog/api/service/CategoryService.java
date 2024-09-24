/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.sys.blog.api.entity.CategoryInfo;
import org.springframework.feign.codec.Response;

/**
 *
 * @author shanhuiming
 *
 */
public interface CategoryService {

    /**
     * 列表
     */
    Response.Page<CategoryInfo> list(CategoryInfo categoryInfo);

    /**
     * 新增
     */
    void add(CategoryInfo categoryInfo);

    /**
     * 修改
     */
    void edit(CategoryInfo categoryInfo);

    /**
     * 删除
     */
    void delete(Long id);
}
