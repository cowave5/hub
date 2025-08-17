/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.infra.sys.mapper.dto.HubAlarmDtoMapper;
import com.cowave.hub.admin.domain.sys.dto.AlarmDto;
import com.cowave.hub.admin.domain.sys.dto.AlarmTypeDto;
import com.cowave.hub.admin.domain.sys.vo.AlarmHandles;
import com.cowave.hub.admin.service.sys.HubAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class HubAlarmServiceImpl implements HubAlarmService {

	private final HubAlarmDtoMapper hubAlarmDtoMapper;

	@Override
	public void add(AlarmDto alarm) {
        if(hubAlarmDtoMapper.alarmIncrease(alarm) == 0) {
            hubAlarmDtoMapper.insert(alarm);
        }
	}

	@Override
	public Page<AlarmTypeDto> typeList(AlarmTypeDto alarmType) {
		return hubAlarmDtoMapper.typeList(Access.page(), alarmType);
	}

	@Override
	public void typeAdd(AlarmTypeDto alarmType) {
	    hubAlarmDtoMapper.insertType(alarmType);
	}

	@Override
	public void typeEdit(AlarmTypeDto alarmType) {
	    hubAlarmDtoMapper.updateType(alarmType);
	}

	@Override
	public void typeDelete(Long id) {
	    hubAlarmDtoMapper.deleteType(id);
	}

	@Override
	public Page<AlarmDto> list(AlarmDto alarm) {
		return hubAlarmDtoMapper.list(Access.page(), alarm);
	}

	@Override
	public AlarmDto info(Long id) {
		return hubAlarmDtoMapper.info(id);
	}

	@Override
	public void delete(Long id) {
	    hubAlarmDtoMapper.delete(id);
	}

    @Override
    public void handle(AlarmHandles alarmHandles) {
        hubAlarmDtoMapper.updateHandle(alarmHandles);
    }
}
