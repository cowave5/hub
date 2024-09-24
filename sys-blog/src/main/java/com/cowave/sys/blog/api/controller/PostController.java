/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.commons.framework.helper.file.FileService;
import com.cowave.sys.blog.api.entity.PostInfo;
import com.cowave.sys.blog.api.service.PostService;
import com.cowave.sys.blog.configuration.BlogConfiguration;
import com.cowave.sys.blog.configuration.ViewConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文章
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final FileService fileService;

    private final BlogConfiguration blogConfiguration;

    /**
     * 上传图片
     */
    @RequestMapping("/upload")
    public Response<String> uploadFile(MultipartFile file) throws Exception {
        String localPath = fileService.localUpload(file, blogConfiguration.getPostImagePath());
        localPath = localPath.replaceFirst(blogConfiguration.getProfile(), ViewConfiguration.RESOURCE_PREFIX);
        return Response.success(localPath);
    }

    /**
     * 上传MD内容图片
     */
    @RequestMapping("/md/upload")
    public Map<String, Object> uploadMdFile(@RequestParam(value = "editormd-image-file") MultipartFile file) throws Exception {
        String localPath = fileService.localUpload(file, blogConfiguration.getMdImagePath());
        localPath = localPath.replaceFirst(blogConfiguration.getProfile(), ViewConfiguration.RESOURCE_PREFIX);
        Map<String, Object> map = new HashMap<>();
        map.put("success", 1);
        map.put("message", "上传成功");
        map.put("url", localPath);
        return map;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Response<Response.Page<PostInfo>> list(PostInfo postInfo) throws Exception {
        return Response.success(postService.list(postInfo));
    }

    /**
     * 新增
     */
    @RequestMapping("/add")
    public Response<Void> add(PostInfo postInfo) throws Exception {
        postService.add(postInfo);
        return Response.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/edit")
    public Response<Void> edit(PostInfo postInfo) throws Exception {
        postService.edit(postInfo);
        return Response.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Response<Void> delete(Long id) {
        postService.delete(id);
        return Response.success();
    }

    /**
     * 导入markdown
     */
    @PostMapping("/importMd")
    public Response<Void> importMd(MultipartFile[] files) throws IOException {
        postService.importMd(files);
        return Response.success();
    }

    /**
     * 导出markdown
     */
    @PostMapping("/exportMd")
    public void exportMd(Long postId, HttpServletResponse response) throws IOException {
        postService.exportMd(postId, response);
    }
}
