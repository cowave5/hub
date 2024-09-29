/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.entity;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 模型字段
 *
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ModelField extends AccessInfo {

    /**
     * id
     */
    private Long id;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 表字段id
     */
    private Long columnId;

    /**
     * 表字段名称
     */
    private String columnName;

    /**
     * 是否主键
     */
    private Integer isPrimary;

    /**
     * 是否自增
     */
    private Integer isIncrement;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private Long fieldType;

    /**
     * 字段类型名称
     */
    private String fieldTypeName;

    /**
     * 字段注释
     */
    private String fieldComment;

    /**
     * 字段排序
     */
    private Integer sort;

    /**
     * 是否非空字段
     */
    private Integer isNotnull;

    /**
     * 是否集合字段
     */
    private Integer isCollect;

    /**
     * 是否插入字段
     */
    private Integer isInsert;

    /**
     * 是否更新字段
     */
    private Integer isEdit;

    /**
     * 是否列表字段
     */
    private Integer isList;

    /**
     * 是否详情字段
     */
    private Integer isInfo;

    /**
     * 是否筛选字段
     */
    private Integer isWhere;

    /**
     * 筛选类型
     */
    private String whereType;

    /**
     * 界面样式
     */
    private String htmlType;

    /**
     * 是否Excel字段
     */
    private Integer isExcel;

    /**
     * Excel列名
     */
    private String excelName;

    /**
     * Excel宽度
     */
    private Integer excelWidth;

    /**
     * Excel转换器
     */
    private String excelConverter;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public String getFieldComment(){
        if(fieldComment == null){
            return fieldName;
        }
        return fieldComment;
    }
}
