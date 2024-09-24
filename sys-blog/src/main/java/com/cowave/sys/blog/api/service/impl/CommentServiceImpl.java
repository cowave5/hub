/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service.impl;

import com.cowave.commons.framework.access.Access;
import com.cowave.sys.blog.api.entity.PostComment;
import com.cowave.sys.blog.api.mapper.CommentMapper;
import com.cowave.sys.blog.api.service.CommentService;
import com.cowave.sys.blog.utils.IpAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author shanhuiming
 *
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
