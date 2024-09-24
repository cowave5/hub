/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.sys.blog.api.entity.CategoryInfo;
import com.cowave.sys.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Response<Response.Page<CategoryInfo>> list(CategoryInfo categoryInfo) throws Exception {
        return Response.success(categoryService.list(categoryInfo));
    }

    /**
     * 新增
     */
    @RequestMapping("/add")
    public Response<Void> add(CategoryInfo categoryInfo) throws Exception {
        categoryService.add(categoryInfo);
        return Response.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/edit")
    public Response<Void> edit(CategoryInfo categoryInfo) throws Exception {
        categoryService.edit(categoryInfo);
        return Response.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return Response.success();
    }
}
