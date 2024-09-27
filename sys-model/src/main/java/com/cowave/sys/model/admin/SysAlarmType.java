/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 告警类型
 *
 * @author shanhuiming
 */
@Data
public class SysAlarmType {

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 类型名称
	 */
	@NotBlank(message = "{valid.notnull.alarm.typename}")
	private String typeName;

	/**
	 * 详情路径
	 */
	private String typeView;

	/**
	 * 描述
	 */
	private String description;
}
