/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.api.service.impl;

import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.blog.api.entity.PostComment;
import com.cowave.sys.blog.api.mapper.CommentMapper;
import com.cowave.sys.blog.api.service.CommentService;
import com.cowave.sys.blog.utils.IpAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public Response.Page<PostComment> getBlogComments(PostComment comment) {
        List<PostComment> commentList = commentMapper.queryByPostId(comment.getPostId());
        commentList.forEach(e -> {
            if (Objects.equals("1",e.getBelow())) {
                e.setReplyComments(commentMapper.queryByPid(e.getId()));
            }
        });
        Response.Page<PostComment> commentPage = new Response.Page<>();
        commentPage.setPage(1);
        commentPage.setPageSize(10);
        commentPage.setList(commentList);
        commentPage.setTotal(commentList.size());
        return commentPage;
    }

    @Override
    public void message(PostComment comment) {
        if (comment.getPId() == null || Objects.equals(0L, comment.getPId())) {
            comment.setAncestors("0");
            comment.setPId(0L);
        } else {
            PostComment postComment = commentMapper.selectById(comment.getPId());
            comment.setAncestors(postComment.getAncestors() + "," + comment.getPId());
            comment.setParentNickName(postComment.getNickName());
        }
        comment.setIp(Access.accessIp());
        comment.setIpAddr(IpAddress.getRemoteAddressByIP(Access.accessIp()));
        comment.setCreateTime(new Date());
        comment.setNickName(Access.userAccount());
        commentMapper.insert(comment);
    }
}
