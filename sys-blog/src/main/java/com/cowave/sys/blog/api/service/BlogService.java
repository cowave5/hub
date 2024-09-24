/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author shanhuiming
 *
 */
public interface BlogService {

    /**
     * 博客首页
     */
    String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception;

    /**
     * 查看全文
     */
    String article(HttpServletRequest request, HttpServletResponse response,
                   ModelMap modelMap, Long articleId, String articlePwd) throws Exception;

    /**
     * 动态
     */
    String dynamics(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception;

    /**
     * 新闻
     */
    String news(ModelMap modelMap);

    /**
     * 留言板
     */
    String comments(ModelMap modelMap);

    /**
     * 聊天室
     */
    String chatRoom(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap);

    /**
     * 归档
     */
    String timeArchives(ModelMap modelMap);

    /**
     * 标签
     */
    String tags(ModelMap modelMap);

    /**
     * 时间轴
     */
    String focus(ModelMap modelMap);

    /**
     * 分类文章
     */
    String categoryPage(ModelMap modelMap, Long categoryId);

    /**
     * 标签文章
     */
    String tagPage(ModelMap modelMap, String tag);

    /**
     * 我的分类
     */
    String ownCategory(ModelMap modelMap);

    /**
     * 我的分类｜修改
     */
    String ownCategoryEdit(Long id, ModelMap modelMap);

    /**
     * 我的博客
     */
    String ownPost(ModelMap modelMap);

    /**
     * 我的博客｜新增
     */
    String ownPostAdd(ModelMap modelMap);

    /**
     * 我的博客｜详情
     */
    String ownPostDetail(Long id, ModelMap modelMap);

    /**
     * 我的博客｜修改
     */
    String ownPostEdit(Long id, ModelMap modelMap);
}
