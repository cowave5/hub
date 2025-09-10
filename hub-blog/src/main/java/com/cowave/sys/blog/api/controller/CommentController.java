/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.blog.api.entity.PostComment;
import com.cowave.sys.blog.api.service.CommentService;
import lombok.RequiredArgsConstructor;
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
