/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.blog.api.entity.PostInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface PostMapper extends BaseMapper<PostInfo> {

    /**
     * 文章列表
     */
    Page<PostInfo> queryPostPage(Page<PostInfo> postPage, @Param("post") PostInfo postInfo);

    /**
     * 文章列表
     */
    Page<PostInfo> queryTagPostPage(Page<PostInfo> postPage, @Param("tag") String tag);

    /**
     * 查询文章
     */
    PostInfo queryPostById(Long id);

    /**
     * 专题文章
     */
    List<PostInfo> queryChannelPosts(Long channelId);

    /**
     * 累计阅读数
     */
    void viewIncrease(Long id);

    /**
     * 时间归档
     */
    Page<PostInfo> timeArchives(Page<PostInfo> page);

    /**
     * 时间轴
     */
    Page<PostInfo> timeArchiving(Page<PostInfo> page);

    /**
     * 新增Post
     */
    void insertPost(PostInfo post);

    /**
     * 新增Post内容
     */
    void insertPostContent(PostInfo post);

    /**
     * 列表
     */
   List<PostInfo> list(PostInfo postInfo);

    /**
     * 计数
     */
   int count(PostInfo postInfo);

    /**
     * 删除Post
     */
    void deletePost(Long id);

    /**
     * 删除Post内容
     */
    void deletePostContent(Long id);

    /**
     * 删除Post标签
     */
    void deletePostTags(Long id);

    /**
     * 更新Post
     */
    void updatePost(PostInfo postInfo);

    /**
     * 更新Post内容
     */
    void updatePostContent(PostInfo postInfo);

    /**
     * 获取文章标签
     */
    List<String> queryTags();

    /**
     * 新增文章标签
     */
    void insertPostTags(@Param("postId") Long postId, @Param("list") List<String> list);
}
