/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.blog.api.entity.PostComment;

/**
 * @author shanhuiming
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
