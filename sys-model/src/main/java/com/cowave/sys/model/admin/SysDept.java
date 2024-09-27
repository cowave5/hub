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
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.AccessUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author shanhuiming
 *
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysDept extends AccessUser {

	/**
	 * 上级部门id
	 */
	private String parentId;

	/**
	 * 部门id
	 */
	private Long deptId;

	/**
	 * 部门编码
	 */
	@ColumnWidth(50)
	@ExcelProperty("部门编码")
	private String deptCode;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "{dept.notnull.name}")
	@ColumnWidth(50)
	@ExcelProperty("部门名称")
	private String deptName;

	/**
	 * 部门简称
	 */
	@ColumnWidth(30)
	@ExcelProperty("部门简称")
	private String deptShort;

	/**
	 * 部门地址
	 */
	@ColumnWidth(50)
	@ExcelProperty("部门地址")
	private String deptAddr;

	/**
	 * 部门电话
	 */
	@ExcelProperty("部门电话")
	private String deptPhone;

	/**
	 * 是否只读
	 */
	private Integer readOnly;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	private String remark;

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
	 * 岗位id列表
	 */
	private List<Long> postIds;

	/**
	 * 部门负责人列表
	 */
	private List<SysUserDept> leaderList;

	/**
	 * 上级部门Id列表
	 */
	@NotEmpty(message = "{dept.notnull.prentIds}")
	private List<Long> parentIds;

	/**
	 * 部门负责人Id列表
	 */
	private List<Long> leaderIds;
}
