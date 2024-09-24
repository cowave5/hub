/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cowave.commons.framework.helper.file.MinioConfiguration;
import com.cowave.commons.tools.Asserts;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.admin.api.service.SysAttachService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cowave.sys.admin.api.mapper.SysAttachMapper;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.commons.framework.helper.file.FileService;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysAttachServiceImpl implements SysAttachService {

    private final MinioConfiguration minioConfiguration;

    private final FileService fileService;

    private final SysAttachMapper sysAttachMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysAttach upload(MultipartFile file, SysAttach sysAttach, boolean isPublic) throws Exception {
        String fileName = file.getOriginalFilename();
        sysAttach.setAttachName(fileName);
        sysAttach.setAttachSize(file.getSize());
        sysAttach.setCreateTime(new Date());
        sysAttach.setIsPublic(isPublic ? 1 : 0);
        String filePath = sysAttach.getAttachType()
                + File.pathSeparator + DateUtils.format("yyyy-MM") + File.pathSeparator + IdUtil.randomUUID() + "." + fileName;
        sysAttach.setAttachPath(filePath);
        sysAttachMapper.insert(sysAttach);

        fileService.minioUpload(file, sysAttach.getAttachGroup(), filePath, isPublic);
        return sysAttach;
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachMapper.info(attachId);
        Asserts.notNull(sysAttach, "attach.notexist");
        return preview(sysAttach);
    }

    @Override
    public String preview(SysAttach sysAttach) throws Exception {
        if(sysAttach.getIsPublic() == 1){
            return minioConfiguration.getEndpoint() + "/" + sysAttach.getAttachGroup() + "/" + sysAttach.getAttachPath();
        }else{
            return fileService.minioPreview(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long attachId) throws Exception {
        if(attachId == null){
            return;
        }
        delete(sysAttachMapper.info(attachId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SysAttach sysAttach) throws Exception {
        if(sysAttach == null){
            return;
        }
        sysAttachMapper.delete(sysAttach.getAttachId());
        fileService.minioDelete(sysAttach.getAttachGroup(), sysAttach.getAttachPath());
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        SysAttach sysAttach = sysAttachMapper.info(attachId);
        Asserts.notNull(sysAttach, "attach.notexist");
        fileService.minioDownload(response, sysAttach.getAttachGroup(), sysAttach.getAttachPath(), sysAttach.getAttachName());
    }

    @Override
    public List<SysAttach> list(Long masterId, String attachGroup, String attachType) {
        return sysAttachMapper.list(masterId, attachGroup, attachType);
    }

    @Override
    public void masterUpdate(List<Long> attachIds, Long masterId) {
        sysAttachMapper.masterUpdate(attachIds, masterId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void masterDelete(Long masterId, String attachGroup, String attachType) throws Exception {
        sysAttachMapper.masterDelete(masterId, attachGroup, attachType);
        List<SysAttach> list = sysAttachMapper.list(masterId, attachGroup, attachType);
        for(SysAttach sysAttach : list) {
            fileService.minioDelete(attachGroup, sysAttach.getAttachPath());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void masterReserve(Long masterId, String attachGroup, String attachType, int reserve) throws Exception {
        List<SysAttach> list = list(masterId, attachGroup, attachType);
        for(int i = reserve; i < list.size(); i++){
            SysAttach attach = list.get(i);
            sysAttachMapper.delete(attach.getAttachId());
            fileService.minioDelete(attachGroup, attach.getAttachPath());
        }
    }

    @Override
    public void updateMasterById(Long masterId, Long attachId) {
        sysAttachMapper.updateMasterById(masterId, attachId);
    }

    @Override
    public SysAttach latestOfMaster(Long masterId, String attachGroup) throws Exception {
        SysAttach attach = sysAttachMapper.latestOfMaster(masterId, attachGroup);
        if(attach != null){
            attach.setViewUrl(preview(attach));
        }
        return attach;
    }
}
