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
import com.cowave.sys.admin.api.entity.flow.Purchase;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
 public interface FlowPurchaseService {

    /**
     * 列表
     */
    Page<Purchase> list(Purchase purchase);

    /**
     * 详情
     */
     Purchase info(Long id);

    /**
     * 新增
     */
     void add(Purchase purchase);

    /**
     * 修改
     */
     void edit(Purchase purchase);

    /**
     * 删除
     */
     void delete(Long[] ids);
}
