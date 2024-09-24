/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.service;

import com.cowave.sys.code.api.entity.ModelInfo;
import com.cowave.sys.code.api.entity.SelectOption;

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
