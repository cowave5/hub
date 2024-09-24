/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.sys.blog.api.entity.PostInfo;
import org.springframework.feign.codec.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author shanhuiming
 *
 */
public interface PostService {

    /**
     * 列表
     */
    Response.Page<PostInfo> list(PostInfo postInfo);

    /**
     * 新增
     */
    void add(PostInfo postInfo);

    /**
     * 修改
     */
    void edit(PostInfo postInfo);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 导入markdown
     */
    void importMd(MultipartFile[] files) throws IOException;

    /**
     * 导出markdown
     */
    void exportMd(Long postId, HttpServletResponse response) throws IOException;
}
