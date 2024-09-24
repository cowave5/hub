/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;

import java.util.HashMap;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class FlowModel {

    /**
     * 流程key
     */
    private String key;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 分类
     */
    private String category;

    /**
     * 描述
     */
    private String description;

    public void fillModelQuery(ModelQuery modelQuery){
        if (StringUtils.isNotEmpty(key)) {
            modelQuery.modelKey(key);
        }
        if (StringUtils.isNotEmpty(name)) {
            modelQuery.modelName(name);
        }
    }

    public void fillModel(Model model){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode metaInfo = objectMapper.createObjectNode();
        metaInfo.put(ModelDataJsonConstants.MODEL_NAME, name);
        metaInfo.put(ModelDataJsonConstants.MODEL_REVISION, version);
        metaInfo.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        model.setMetaInfo(metaInfo.toString());
        model.setKey(key);
        model.setName(name);
        model.setVersion(version);
        model.setCategory(category);
    }

    public byte[] buildModelContent(String modelId) throws JsonProcessingException {
        HashMap<String, String> stencilset = new HashMap<>();
        stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

        HashMap<String, String> properties = new HashMap<>();
        properties.put("process_id", key);
        properties.put("name", name);
        properties.put("category", category);

        HashMap<String, Object> content = new HashMap<>();
        content.put("resourceId", modelId);
        content.put("properties", properties);
        content.put("stencilset", stencilset);
        return new ObjectMapper().writeValueAsBytes(content);
    }
}
