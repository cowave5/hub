/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.minio.MinioHelper;
import com.cowave.commons.framework.helper.minio.MinioProperties;
import com.cowave.commons.tools.DateUtils;
import com.cowave.hub.admin.domain.sys.HubAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.request.AttachQuery;
import com.cowave.hub.admin.domain.sys.request.AttachUpload;
import com.cowave.hub.admin.infra.sys.dao.HubAttachDao;
import com.cowave.hub.admin.infra.sys.dao.HubNoticeDao;
import com.cowave.hub.admin.infra.rabc.dao.HubTenantDao;
import com.cowave.hub.admin.infra.rabc.dao.HubUserDao;
import com.cowave.hub.admin.service.sys.HubAttachService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;
import static com.cowave.hub.admin.domain.rabc.enums.YesNo.YES;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubAttachServiceImpl implements HubAttachService {
    private final MinioProperties minioProperties;
    private final MinioHelper minioHelper;
    private final HubAttachDao hubAttachDao;
    private final HubUserDao hubUserDao;
    private final HubTenantDao hubTenantDao;
    private final HubNoticeDao hubNoticeDao;

    @Override
    public Page<HubAttach> page(String tenantId, AttachQuery query) {
        Page<HubAttach> page = hubAttachDao.page(query);
        for (HubAttach attach : page.getRecords()) {
            String ownerId = attach.getOwnerId();
            if(StringUtils.isBlank(ownerId)){
                continue;
            }

            String ownerName = null;
            String module = attach.getOwnerModule();
            if(SYSTEM_USER.equals(module)){
                ownerName = hubUserDao.queryNameById(Integer.valueOf(ownerId));
            } else if (SYSTEM_NOTICE.equals(module)) {
                ownerName = hubNoticeDao.queryNameById(Long.valueOf(ownerId));
            } else if (SYSTEM_TENANT.equals(module)) {
                ownerName = hubTenantDao.queryNameById(ownerId);
            }
            attach.setOwnerName(ownerName);
        }
        return page;
    }

    @Override
    public HubAttach upload(MultipartFile file, AttachUpload upload) throws Exception {
        String fileName = file.getOriginalFilename();
        HubAttach hubAttach = HubAttach.builder()
                .attachName(fileName)
                .attachSize(file.getSize())
                .ownerId(upload.getOwnerId())
                .ownerModule(upload.getOwnerModule())
                .attachType(upload.getAttachType())
                .isPrivate(upload.getIsPrivate())
                .createBy(Access.userCode())
                .updateBy(Access.userCode())
                .createTime(Access.accessTime())
                .updateTime(Access.accessTime())
                .build();
        hubAttach.setTenantId(upload.getTenantId());
        if (StringUtils.isBlank(upload.getTenantId())) {
            hubAttach.setTenantId(Access.tenantId());
        }
        String filePath = upload.getAttachType() + File.separator
                + DateUtils.format("yyyy-MM") + File.separator + IdUtil.randomUUID() + "." + fileName;
        hubAttach.setAttachPath(filePath);
        hubAttachDao.save(hubAttach);

        minioHelper.upload(file, hubAttach.getTenantId(), filePath, YES == upload.getIsPrivate());
        hubAttach.setViewUrl(preview(hubAttach));
        return hubAttach;
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        HubAttach hubAttach = hubAttachDao.getById(attachId);
        HttpAsserts.notNull(hubAttach, NOT_FOUND, "{admin.attach.not.exist}");
        minioHelper.download(response, hubAttach.getTenantId(), hubAttach.getAttachPath(), hubAttach.getAttachName());
    }

    @Override
    public String preview(Long attachId) throws Exception {
        HubAttach hubAttach = hubAttachDao.getById(attachId);
        HttpAsserts.notNull(hubAttach, NOT_FOUND, "{admin.attach.not.exist}");
        return preview(hubAttach);
    }

    @Override
    public String preview(HubAttach hubAttach) throws Exception {
        if (YES == hubAttach.getIsPrivate()) {
            return minioHelper.preview(hubAttach.getTenantId(), hubAttach.getAttachPath());
        } else {
            return minioProperties.getEndpoint() + File.separator + hubAttach.getTenantId() + File.separator + hubAttach.getAttachPath();
        }
    }

    @Override
    public void delete(List<Long> attachIds) throws Exception {
        for(Long attachId : attachIds){
            HubAttach hubAttach = hubAttachDao.getById(attachId);
            HttpAsserts.isBlank(hubAttach.getOwnerId(), BAD_REQUEST,
                    "{admin.attach.delete}", hubAttach.getAttachName());
            remove(hubAttach);
        }
    }

    @Override
    public void remove(HubAttach hubAttach) throws Exception {
        if (hubAttach == null) {
            return;
        }
        hubAttachDao.removeById(hubAttach.getAttachId());
        minioHelper.delete(hubAttach.getTenantId(), hubAttach.getAttachPath());
    }

    @Override
    public void removeById(Long attachId) throws Exception {
        remove(hubAttachDao.getById(attachId));
    }

    @Override
    public HubAttach latestOfOwner(String ownerId, String ownerModule, AttachType attachType) throws Exception {
        HubAttach attach = hubAttachDao.latestOfOwner(ownerId, ownerModule, attachType);
        if (attach != null) {
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }

    @Override
    public void reserveByOwner(String ownerId, String ownerModule, AttachType attachType, int reserve) throws Exception {
        List<HubAttach> list = hubAttachDao.listOfOwner(ownerId, ownerModule, attachType);
        for (int i = reserve; i < list.size(); i++) {
            HubAttach attach = list.get(i);
            hubAttachDao.removeById(attach.getAttachId());
            minioHelper.delete(attach.getTenantId(), attach.getAttachPath());
        }
    }
}
