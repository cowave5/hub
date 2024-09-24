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
import com.cowave.sys.admin.api.entity.flow.Leave;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface FlowLeaveService {

    /**
     * 列表
     */
    Page<Leave> list(Leave leave);

    /**
     * 详情
     */
    Leave info(Long id);

    /**
     * 新增请假
     */
    void add(Leave leave);

    /**
     * 修改请假
     */
    int edit(Leave leave);

    /**
     * 删除请假
     */
    void delete(Long[] ids);

    /**
     * 撤销
     */
    void revocate(Long id);
}
