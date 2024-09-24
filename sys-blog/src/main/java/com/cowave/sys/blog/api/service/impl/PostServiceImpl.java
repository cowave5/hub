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
import com.cowave.sys.blog.api.cache.BlogCache;
import com.cowave.sys.blog.api.entity.PostInfo;
import com.cowave.sys.blog.api.mapper.PostMapper;
import com.cowave.sys.blog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final BlogCache blogCache;

    private final PostMapper postMapper;

    @Override
    public Response.Page<PostInfo> list(PostInfo postInfo) {
        postInfo.setCreateUser(Access.userAccount());
        return new Response.Page<>(postMapper.list(postInfo), postMapper.count(postInfo));
    }

    @CacheEvict(value = "blogs", allEntries = true, cacheManager = "ehcacheCacheManager")
    @Override
    public void add(PostInfo postInfo) {
        postInfo.setCreateTime(Access.accessTime());
        postInfo.setUpdateTime(Access.accessTime());
        postMapper.insertPost(postInfo);
        postMapper.insertPostContent(postInfo);
        String tags = postInfo.getTags();
        if(StringUtils.isNotBlank(tags)){
            List<String> tagList = Arrays.stream(tags.split(",")).map(String::trim).toList();
            postMapper.insertPostTags(postInfo.getId(), tagList);
        }
        // 删除Html缓存
        blogCache.removeHtmlOfPrefix("blog-");
    }

    @CacheEvict(value = "blogs", allEntries = true, cacheManager = "ehcacheCacheManager")
    @Override
    public void edit(PostInfo postInfo) {
        postInfo.setUpdateTime(Access.accessTime());
        postMapper.updatePost(postInfo);
        postMapper.updatePostContent(postInfo);
        postMapper.deletePostTags(postInfo.getId());
        String tags = postInfo.getTags();
        if(StringUtils.isNotBlank(tags)){
            List<String> tagList = Arrays.stream(tags.split(",")).map(String::trim).toList();
            postMapper.insertPostTags(postInfo.getId(), tagList);
        }
        // 删除Html缓存
        blogCache.removeHtml(postInfo.getId());
        blogCache.removeHtmlOfPrefix("blog-");
    }

    @CacheEvict(value = "blogs", allEntries = true, cacheManager = "ehcacheCacheManager")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        postMapper.deletePost(id);
        postMapper.deletePostContent(id);
        postMapper.deletePostTags(id);
        // 删除Html缓存
        blogCache.removeHtml(id);
        blogCache.removeHtmlOfPrefix("blog-");
    }

    @CacheEvict(value = "blogs", allEntries = true, cacheManager = "ehcacheCacheManager")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importMd(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String mdName = fileName.substring(0, fileName.lastIndexOf("."));
            PostInfo postInfo = new PostInfo();
            postInfo.setTitle(mdName);
            postInfo.setFeatured(0);
            postInfo.setSlider(0);
            postInfo.setStatus(1);
            postInfo.setCreateUser(Access.userAccount());
            postInfo.setCreateTime(Access.accessTime());
            postInfo.setUpdateTime(Access.accessTime());
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))){
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                postInfo.setContent(builder.toString());
            }
            postMapper.insertPost(postInfo);
            postMapper.insertPostContent(postInfo);
        }
        // 删除Html缓存
        blogCache.removeHtmlOfPrefix("blog-");
    }

    @Override
    public void exportMd(Long postId, HttpServletResponse response) throws IOException {
        PostInfo postInfo = postMapper.queryPostById(postId);
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("markdown");
        try(PrintWriter writer = response.getWriter()){
            writer.write(postInfo.getContent());
        }
    }
}
