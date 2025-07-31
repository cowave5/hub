/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.service.code;

import com.cowave.sys.meter.domain.code.AppInfo;

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
