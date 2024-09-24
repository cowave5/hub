/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.sys.blog.api.entity.PostComment;
import org.springframework.feign.codec.Response;

/**
 *
 * @author shanhuiming
 *
 */
public interface CommentService {

    /**
     * 博客评论
     */
    Response.Page<PostComment> getBlogComments(PostComment comment);

    /**
     * 新增评论
     */
    void message(PostComment comment);
}
