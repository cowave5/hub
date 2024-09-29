/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao.mapper.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.base.vo.AlarmHandles;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.domain.base.dto.SysAlarmTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysAlarmDtoMapper {

	/**
	 * 类型列表
	 */
    Page<SysAlarmTypeDto> typeList(Page<SysAlarmTypeDto> page, @Param("type") SysAlarmTypeDto sysAlarmType);

	/**
	 * 类型新增
	 */
	void insertType(SysAlarmTypeDto sysAlarmType);

	/**
	 * 类型更新
	 */
	void updateType(SysAlarmTypeDto sysAlarmType);

    /**
     * 类型下存在告警
     */
    SysAlarmDto selectOne(Long id);

    /**
     * 类型删除
     */
    void deleteType(Long id);

    /**
     * 累计
     */
    int alarmIncrease(SysAlarmDto sysAlarm);

	/**
     * 新增
     */
    void insert(SysAlarmDto sysAlarm);

    /**
     * 列表
     */
    Page<SysAlarmDto> list(Page<SysAlarmDto> page, @Param("alarm") SysAlarmDto sysAlarm);

    /**
     * 详情
     */
    SysAlarmDto info(long id);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 处理
     */
    void updateHandle(AlarmHandles alarmHandles);
}
