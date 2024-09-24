/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.blog.api.entity.CategoryInfo;
import com.cowave.sys.blog.api.mapper.CategoryMapper;
import com.cowave.sys.blog.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public Response.Page<CategoryInfo> list(CategoryInfo categoryInfo) {
        QueryWrapper<CategoryInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(categoryInfo.getName())){
            queryWrapper.lambda().like(CategoryInfo::getName, categoryInfo.getName());
        }
        Page<CategoryInfo> page = categoryMapper.selectPage(Access.page(), queryWrapper);
        Response.Page<CategoryInfo> categoryPage = new Response.Page<>();
        categoryPage.setList(page.getRecords());
        categoryPage.setTotal(page.getTotal());
        categoryPage.setPage(Access.pageIndex());
        categoryPage.setPageSize(Access.pageSize());
        categoryPage.setTotalPage(page.getPages());
        return categoryPage;
    }

    @Override
    public void add(CategoryInfo categoryInfo) {
        categoryMapper.insert(categoryInfo);
    }

    @Override
    public void edit(CategoryInfo categoryInfo) {
        categoryMapper.updateById(categoryInfo);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
