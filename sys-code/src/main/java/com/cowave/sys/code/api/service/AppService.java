/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service;

import com.cowave.sys.code.api.entity.AppInfo;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author shanhuiming
 */
public interface AppService {

    /**
     * 列表
     */
    List<AppInfo> list(AppInfo appInfo);

    /**
     * 详情
     */
    AppInfo info(Long id);

    /**
     * 新增
     */
    void add(AppInfo appInfo);

    /**
     * 编辑
     */
    void edit(AppInfo appInfo);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 工程模板
     */
    byte[] template(Long id) throws IOException;
}
