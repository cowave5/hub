/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.flow.Meeting;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface FlowMeetingService {

    /**
     * 列表
     */
    Page<Meeting> list(Meeting meeting);

    /**
     * 详情
     */
    Meeting info(Long id);

    /**
     * 新增
     */
    void add(Meeting meeting);

    /**
     * 修改
     */
    void edit(Meeting meeting);

    /**
     * 删除
     */
    void delete(Long[] ids);
}
