/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import cn.hutool.core.util.IdUtil;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.minio.MinioHelper;
import com.cowave.commons.framework.helper.minio.MinioProperties;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.request.AttachQuery;
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysAttachServiceImpl implements SysAttachService {
    private final MinioHelper minioHelper;
    private final MinioProperties minioProperties;
    private final SysAttachDao sysAttachDao;

    @Override
    public List<SysAttach> list(String tenantId, AttachQuery query) {
        return sysAttachDao.queryList(query.getOwnerId(), query.getOwnerType(), query.getAttachType());
    }

    @Override
    public SysAttach upload(MultipartFile file, AttachUpload upload) throws Exception {
        String fileName = file.getOriginalFilename();
        SysAttach sysAttach = SysAttach.builder()
                .attachName(fileName)
                .attachSize(file.getSize())
                .ownerId(upload.getOwnerId())
                .ownerType(upload.getOwnerType())
                .attachType(upload.getAttachType())
                .isPrivate(upload.getIsPrivate())
                .createBy(Access.userCode())
                .updateBy(Access.userCode())
                .createTime(Access.accessTime())
                .updateTime(Access.accessTime())
                .build();
        sysAttach.setTenantId(upload.getTenantId());
        if (StringUtils.isBlank(upload.getTenantId())) {
            sysAttach.setTenantId(Access.tenantId());
        }
        String filePath = upload.getAttachType() + File.separator
                + DateUtils.format("yyyy-MM") + File.separator + IdUtil.randomUUID() + "." + fileName;
        sysAttach.setAttachPath(filePath);
        sysAttachDao.save(sysAttach);

        minioHelper.upload(file, sysAttach.getTenantId(), filePath, upload.getIsPrivate() == 1);
        sysAttach.setViewUrl(preview(sysAttach));
        return sysAttach;
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachDao.getById(attachId);
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.notexist}");
        minioHelper.download(response, sysAttach.getOwnerType(), sysAttach.getAttachPath(), sysAttach.getAttachName());
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachDao.getById(attachId);
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.notexist}");
        return preview(sysAttach);
    }

    @Override
    public String preview(SysAttach sysAttach) throws Exception {
        if (sysAttach.getIsPrivate() == 1) {
            return minioHelper.preview(sysAttach.getTenantId(), sysAttach.getAttachPath());
        } else {
            return minioProperties.getEndpoint() + File.separator + sysAttach.getTenantId() + File.separator + sysAttach.getAttachPath();
        }
    }

    @Override
    public void delete(Long attachId) throws Exception {
        if (attachId == null) {
            return;
        }
        delete(sysAttachDao.getById(attachId));
    }

    @Override
    public void delete(SysAttach sysAttach) throws Exception {
        if (sysAttach == null) {
            return;
        }
        sysAttachDao.removeById(sysAttach.getAttachId());
        minioHelper.delete(sysAttach.getOwnerType(), sysAttach.getAttachPath());
    }

    @Override
    public SysAttach latestOfOwner(String ownerId, String ownerType, String attachType) throws Exception {
        SysAttach attach = sysAttachDao.latestOfOwner(ownerId, ownerType, attachType);
        if (attach != null) {
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }

    @Override
    public void masterReserve(String ownerId, String ownerType, String attachType, int reserve) throws Exception {
        List<SysAttach> list = sysAttachDao.queryList(ownerId, ownerType, attachType);
        for (int i = reserve; i < list.size(); i++) {
            SysAttach attach = list.get(i);
            sysAttachDao.removeById(attach.getAttachId());
            minioHelper.delete(ownerType, attach.getAttachPath());
        }
    }
}
