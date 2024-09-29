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
import com.cowave.commons.framework.helper.minio.MinioHelper;
import com.cowave.commons.framework.helper.minio.MinioProperties;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.admin.domain.base.SysAttach;
import com.cowave.sys.admin.domain.base.request.AttachQuery;
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.service.base.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
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
    public List<SysAttach> list(AttachQuery query) {
        return sysAttachDao.queryList(query.getMasterId(), query.getAttachGroup(), query.getAttachType());
    }

    @Override
    public SysAttach upload(MultipartFile file, AttachUpload upload) throws Exception {
        String fileName = file.getOriginalFilename();

        SysAttach sysAttach = new SysAttach();
        sysAttach.setAttachName(fileName);
        sysAttach.setAttachSize(file.getSize());
        sysAttach.setCreateTime(new Date());
        sysAttach.setMasterId(upload.getMasterId());
        sysAttach.setIsPublic(upload.getIsPublic());
        sysAttach.setAttachType(upload.getAttachType());
        sysAttach.setAttachGroup(upload.getAttachGroup());

        String filePath = upload.getAttachType() + File.pathSeparator
                + DateUtils.format("yyyy-MM") + File.pathSeparator + IdUtil.randomUUID() + "." + fileName;
        sysAttach.setAttachPath(filePath);

        sysAttachDao.save(sysAttach);
        minioHelper.upload(file, upload.getAttachGroup(), filePath, upload.getIsPublic() == 1);
        return sysAttach;
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachDao.getById(attachId);
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.notexist}");
        minioHelper.download(response, sysAttach.getAttachGroup(), sysAttach.getAttachPath(), sysAttach.getAttachName());
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachDao.getById(attachId);
        HttpAsserts.notNull(sysAttach, NOT_FOUND, "{admin.attach.notexist}");
        return preview(sysAttach);
    }

    @Override
    public String preview(SysAttach sysAttach) throws Exception {
        if(sysAttach.getIsPublic() == 1){
            return minioProperties.getEndpoint() + "/" + sysAttach.getAttachGroup() + "/" + sysAttach.getAttachPath();
        }else{
            return minioHelper.preview(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
        }
    }

    @Override
    public void delete(Long attachId) throws Exception {
        if(attachId == null){
            return;
        }
        delete(sysAttachDao.getById(attachId));
    }

    @Override
    public void delete(SysAttach sysAttach) throws Exception {
        if(sysAttach == null){
            return;
        }
        sysAttachDao.removeById(sysAttach.getAttachId());
        minioHelper.delete(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
    }

    @Override
    public void updateMasterById(Long masterId, Long attachId) {
        sysAttachDao.updateMasterById(masterId, attachId);
    }

    @Override
    public SysAttach latestOfMaster(Long masterId, String attachGroup) throws Exception {
        SysAttach attach = sysAttachDao.latestOfMaster(masterId, attachGroup);
        if(attach != null){
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }

    @Override
    public void masterReserve(Long masterId, String attachGroup, String attachType, int reserve) throws Exception {
        List<SysAttach> list = sysAttachDao.queryList(masterId, attachGroup, attachType);
        for(int i = reserve; i < list.size(); i++){
            SysAttach attach = list.get(i);
            sysAttachDao.removeById(attach.getAttachId());
            minioHelper.delete(attachGroup, attach.getAttachPath());
        }
    }
}
