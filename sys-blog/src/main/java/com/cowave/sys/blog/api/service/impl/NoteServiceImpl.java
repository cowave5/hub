/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.blog.api.entity.NoteInfo;
import com.cowave.sys.blog.api.mapper.NoteMapper;
import com.cowave.sys.blog.api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteMapper noteMapper;

    @Override
    public Response.Page<NoteInfo> notePage(ModelMap modelMap) {
        QueryWrapper<NoteInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(NoteInfo::getNoteType, "-1")
                .and(e -> e.eq(NoteInfo::getIsPublic, "0")).orderByDesc(NoteInfo::getCreateTime);
        Page<NoteInfo> blogNotePage = noteMapper.selectPage(Access.page(), queryWrapper);
        Response.Page<NoteInfo> notePage = new Response.Page<>();
        notePage.setList(blogNotePage.getRecords());
        notePage.setTotal(blogNotePage.getTotal());
        notePage.setTotalPage(blogNotePage.getPages());
        notePage.setPage(Access.pageIndex());
        notePage.setPageSize(Access.pageSize());
        return notePage;
    }

}
