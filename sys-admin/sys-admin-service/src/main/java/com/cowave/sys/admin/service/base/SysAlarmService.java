/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.base.vo.AlarmHandles;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.domain.base.dto.SysAlarmTypeDto;

/**
 * @author shanhuiming
 */
public interface SysAlarmService {

	/**
	 * 新增
	 */
	void add(SysAlarmDto sysAlarm);

	/**
	 * 类型列表
	 */
	Page<SysAlarmTypeDto> typeList(SysAlarmTypeDto sysAlarmType);

	/**
	 * 类型新增
	 */
	void typeAdd(SysAlarmTypeDto sysAlarmType);

	/**
	 * 类型修改
	 */
	void typeEdit(SysAlarmTypeDto sysAlarmType);

	/**
	 * 类型删除
	 */
	void typeDelete(Long id);

	/**
	 * 列表
	 */
	Page<SysAlarmDto> list(SysAlarmDto sysAlarm);

	/**
	 * 详情
	 */
	SysAlarmDto info(Long id);

	/**
	 * 删除
	 */
	void delete(Long id);

	/**
	 * 处理
	 */
	void handle(AlarmHandles alarmHandles);
}
