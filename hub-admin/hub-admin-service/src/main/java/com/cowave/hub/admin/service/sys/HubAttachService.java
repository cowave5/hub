/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.HubAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.request.AttachQuery;
import com.cowave.hub.admin.domain.sys.request.AttachUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubAttachService {

    /**
     * 分页
     */
    Page<HubAttach> page(String tenantId, AttachQuery query);

    /**
     * 上传
     */
    HubAttach upload(MultipartFile file, AttachUpload attachUpload) throws Exception;

    /**
     * 下载
     */
    void download(HttpServletResponse response, Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(HubAttach hubAttach) throws Exception;

    /**
     * 删除
     */
    void delete(List<Long> attachIds) throws Exception;

    /**
     * 删除
     */
    void remove(HubAttach hubAttach) throws Exception;

    /**
     * 删除
     */
    void removeById(Long attachId) throws Exception;

    /**
     * 宿主最新附件
     */
    HubAttach latestOfOwner(String ownerId, String ownerModule, AttachType attachType) throws Exception;

    /**
     * 保留最近几个
     */
    void reserveByOwner(String ownerId, String ownerModule, AttachType attachType, int reserve) throws Exception;
}
