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

import com.cowave.sys.meter.domain.code.ModelInfo;
import com.cowave.sys.meter.domain.code.SelectOption;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 */
public interface ModelService {

    /**
     * 模型选项
     */
    List<SelectOption> options();

    /**
     * 列表
     */
    List<ModelInfo> list(ModelInfo modelInfo);

    /**
     * 详情
     */
    ModelInfo info(Long id);

    /**
     * 新增
     */
    void add(ModelInfo modelInfo);

    /**
     * 编辑
     */
    void edit(ModelInfo modelInfo);

    /**
     * 删除
     */
    void delete(Integer[] id);

    /**
     * 是否导出Excel切换
     */
    void switchExcel(Long id, Integer flag);

    /**
     * 是否继承Access切换
     */
    void switchAccess(Long id, Integer flag);

    /**
     * 是否记录日志切换
     */
    void switchLog(Long id, Integer flag);

    /**
     * 生成模型
     */
    void generate(ModelInfo modelInfo);

    /**
     * 代码预览
     */
    Map<String, String> preview(Long id);

    /**
     * 代码预览
     */
    Map<String, String> preview(ModelInfo model);

    /**
     * 代码模板
     */
    byte[] template(Long id) throws IOException;
}
