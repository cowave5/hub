/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.sys.mapper.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.vo.AlarmHandles;
import com.cowave.hub.admin.domain.sys.dto.AlarmDto;
import com.cowave.hub.admin.domain.sys.dto.AlarmTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubAlarmDtoMapper {

	/**
	 * 类型列表
	 */
    Page<AlarmTypeDto> typeList(Page<AlarmTypeDto> page, @Param("type") AlarmTypeDto alarmType);

	/**
	 * 类型新增
	 */
	void insertType(AlarmTypeDto alarmType);

	/**
	 * 类型更新
	 */
	void updateType(AlarmTypeDto alarmType);

    /**
     * 类型下存在告警
     */
    AlarmDto selectOne(Long id);

    /**
     * 类型删除
     */
    void deleteType(Long id);

    /**
     * 累计
     */
    int alarmIncrease(AlarmDto alarm);

	/**
     * 新增
     */
    void insert(AlarmDto alarm);

    /**
     * 列表
     */
    Page<AlarmDto> list(Page<AlarmDto> page, @Param("alarm") AlarmDto alarm);

    /**
     * 详情
     */
    AlarmDto info(long id);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 处理
     */
    void updateHandle(AlarmHandles alarmHandles);
}
