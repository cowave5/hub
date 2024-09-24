/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.AlarmHandles;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysAlarmType;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysAlarmService {

	/**
	 * 类型列表
	 */
	Page<SysAlarmType> typeList(SysAlarmType sysAlarmType);

	/**
	 * 类型新增
	 */
	void typeAdd(SysAlarmType sysAlarmType);

	/**
	 * 类型修改
	 */
	void typeEdit(SysAlarmType sysAlarmType);

	/**
	 * 类型删除
	 */
	void typeDelete(Long id);

	/**
	 * 列表
	 */
	Page<SysAlarm> list(SysAlarm sysAlarm);

	/**
	 * 详情
	 */
	SysAlarm info(Long id);

	/**
	 * 删除
	 */
	void delete(Long id);

	/**
	 * 处理
	 */
	void handle(AlarmHandles alarmHandles);
}
