/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.cowave.commons.framework.support.excel.StringConverter;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class SysDict {

    /**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 上级字典码
	 */
	private String parentCode;

    /**
     * 字典码
     */
	@ExcelProperty("字典码")
    private String dictCode;

    /**
	 * 字典名称
	 */
	@ExcelProperty("字典名称")
	private String dictName;

    /**
     * 字典值
     */
	@ExcelProperty(value = "字典值", converter = StringConverter.class)
    private Object dictValue;

	/**
	 * 值类型
	 */
	@ExcelProperty(value = "值类型")
	private String valueType;

	/**
	 * 值转换器
	 */
	@ColumnWidth(60)
	@ExcelProperty(value = "值转换器")
	private String valueParser;

    /**
	 * 字典排序
	 */
	@ExcelProperty("字典排序")
	private Integer dictOrder;

	/**
	 * css样式
	 */
	private String css;

    /**
     * 是否默认值
     */
	@ExcelProperty(value = "是否默认值", converter = YesNoConverter.class)
    private Integer isDefault = 0;

	/**
     * 状态
     */
	@ExcelProperty(value = "字典状态", converter = StatusConverter.class)
    private Integer status;

    /**
	 * 备注
	 */
	@ColumnWidth(80)
	@ExcelProperty("备注")
	private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
