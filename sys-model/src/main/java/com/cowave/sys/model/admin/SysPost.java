/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.AccessUser;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysPost extends AccessUser {

	/**
	 * 部门id
	 */
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 上级岗位id
	 */
	private Long parentId;

	/**
	 * 岗位id
	 */
	private Long postId;

	/**
	 * 岗位编码
	 */
	@ColumnWidth(50)
	@ExcelProperty(value = "岗位编码")
	private String postCode;

	/**
	 * 岗位类型
	 */
	@ExcelProperty(value = "岗位类型")
	private String postType;

	/**
	 * 岗位名称
	 */
	@NotBlank(message = "{post.notnull.name}")
	@ExcelProperty(value = "岗位名称")
	private String postName;

	/**
	 * 岗位级别
	 */
	private Integer postLevel;

	/**
	 * 岗位状态
	 */
	@ExcelProperty(value = "岗位状态", converter = StatusConverter.class)
	private Integer postStatus;

	/**
	 * 是否只读
	 */
	private Integer readOnly;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建部门
	 */
	private Long createDept;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新部门
	 */
	private Long updateDept;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	private String remark;

}
