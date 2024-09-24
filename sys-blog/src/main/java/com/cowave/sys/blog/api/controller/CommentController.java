/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.sys.blog.api.entity.PostComment;
import com.cowave.sys.blog.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 留言
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = {"/comment"})
public class CommentController {

    private final CommentService commentService;

    /**
     * 博客评论
     */
    @PostMapping("/list")
    public Response<Response.Page<PostComment>> getBlogMessage(PostComment comment) {
        return Response.success(commentService.getBlogComments(comment));
    }

    /**
     * 新增评论
     */
    @PostMapping("/message")
    public Response<Void> message(PostComment comment) {
        commentService.message(comment);
        return Response.success();
    }
}
