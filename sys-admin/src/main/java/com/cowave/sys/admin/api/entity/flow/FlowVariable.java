/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class FlowVariable {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 执行
     */
    private String executionId;

    /**
     * 变量id
     */
    private String variableId;

    /**
     * 变量名称
     */
    private String variableName;

    /**
     * 变量类型
     */
    private String variableTypeName;

    /**
     * 变量值
     */
    private Object value;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatedTime;

    /**
     * 修改按钮控制
     */
    private boolean edit;
}
