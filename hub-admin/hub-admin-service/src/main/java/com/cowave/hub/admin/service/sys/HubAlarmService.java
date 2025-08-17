/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.vo.AlarmHandles;
import com.cowave.hub.admin.domain.sys.dto.AlarmDto;
import com.cowave.hub.admin.domain.sys.dto.AlarmTypeDto;

/**
 * @author shanhuiming
 */
public interface HubAlarmService {

	/**
	 * 新增
	 */
	void add(AlarmDto alarm);

	/**
	 * 类型列表
	 */
	Page<AlarmTypeDto> typeList(AlarmTypeDto alarmType);

	/**
	 * 类型新增
	 */
	void typeAdd(AlarmTypeDto alarmType);

	/**
	 * 类型修改
	 */
	void typeEdit(AlarmTypeDto alarmType);

	/**
	 * 类型删除
	 */
	void typeDelete(Long id);

	/**
	 * 列表
	 */
	Page<AlarmDto> list(AlarmDto alarm);

	/**
	 * 详情
	 */
	AlarmDto info(Long id);

	/**
	 * 删除
	 */
	void delete(Long id);

	/**
	 * 处理
	 */
	void handle(AlarmHandles alarmHandles);
}
