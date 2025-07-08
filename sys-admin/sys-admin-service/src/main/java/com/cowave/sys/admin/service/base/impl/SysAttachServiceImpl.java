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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.minio.MinioHelper;
import com.cowave.commons.framework.helper.minio.MinioProperties;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.request.AttachQuery;
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.infra.base.dao.SysNoticeDao;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDao;
import com.cowave.sys.admin.service.base.SysAttachService;
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
import static com.cowave.sys.admin.domain.base.constants.OpModule.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysAttachServiceImpl implements SysAttachService {
    private final MinioProperties minioProperties;
    private final MinioHelper minioHelper;
    private final SysAttachDao sysAttachDao;
    private final SysUserDao sysUserDao;
    private final SysTenantDao sysTenantDao;
    private final SysNoticeDao sysNoticeDao;

    @Override
    public Page<SysAttach> page(String tenantId, AttachQuery query) {
        Page<SysAttach> page = sysAttachDao.page(query);
        for (SysAttach attach : page.getRecords()) {
            String ownerId = attach.getOwnerId();
            if(StringUtils.isBlank(ownerId)){
                continue;
            }

            String ownerName = null;
            String module = attach.getOwnerModule();
            if(SYSTEM_USER.equals(module)){
                ownerName = sysUserDao.queryNameById(Integer.valueOf(ownerId));
            } else if (SYSTEM_NOTICE.equals(module)) {
                ownerName = sysNoticeDao.queryNameById(Long.valueOf(ownerId));
            } else if (SYSTEM_TENANT.equals(module)) {
                ownerName = sysTenantDao.queryNameById(ownerId);
            }
            attach.setOwnerName(ownerName);
        }
        return page;
    }

    @Override
    public SysAttach upload(MultipartFile file, AttachUpload upload) throws Exception {
        String fileName = file.getOriginalFilename();
        SysAttach sysAttach = SysAttach.builder()
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
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.not.exist}");
        minioHelper.download(response, sysAttach.getTenantId(), sysAttach.getAttachPath(), sysAttach.getAttachName());
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachDao.getById(attachId);
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.not.exist}");
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
    public void delete(List<Long> attachIds) throws Exception {
        for(Long attachId : attachIds){
            SysAttach sysAttach = sysAttachDao.getById(attachId);
            HttpAsserts.isBlank(sysAttach.getOwnerId(), BAD_REQUEST,
                    "{admin.attach.delete}", sysAttach.getAttachName());
            remove(sysAttach);
        }
    }

    @Override
    public void remove(SysAttach sysAttach) throws Exception {
        if (sysAttach == null) {
            return;
        }
        sysAttachDao.removeById(sysAttach.getAttachId());
        minioHelper.delete(sysAttach.getTenantId(), sysAttach.getAttachPath());
    }

    @Override
    public void removeById(Long attachId) throws Exception {
        remove(sysAttachDao.getById(attachId));
    }

    @Override
    public SysAttach latestOfOwner(String ownerId, String ownerModule, String attachType) throws Exception {
        SysAttach attach = sysAttachDao.latestOfOwner(ownerId, ownerModule, attachType);
        if (attach != null) {
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }

    @Override
    public void reserveByOwner(String ownerId, String ownerModule, String attachType, int reserve) throws Exception {
        List<SysAttach> list = sysAttachDao.listOfOwner(ownerId, ownerModule, attachType);
        for (int i = reserve; i < list.size(); i++) {
            SysAttach attach = list.get(i);
            sysAttachDao.removeById(attach.getAttachId());
            minioHelper.delete(attach.getTenantId(), attach.getAttachPath());
        }
    }
}
