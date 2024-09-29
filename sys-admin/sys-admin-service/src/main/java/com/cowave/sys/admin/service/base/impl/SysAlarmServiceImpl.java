/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.infra.base.dao.mapper.dto.SysAlarmDtoMapper;
import com.cowave.sys.admin.domain.base.dto.SysAlarmDto;
import com.cowave.sys.admin.domain.base.dto.SysAlarmTypeDto;
import com.cowave.sys.admin.domain.base.vo.AlarmHandles;
import com.cowave.sys.admin.service.base.SysAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysAlarmServiceImpl implements SysAlarmService {

	private final SysAlarmDtoMapper sysAlarmMapper;

	@Override
	public void add(SysAlarmDto sysAlarm) {
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
	}

	@Override
	public Page<SysAlarmTypeDto> typeList(SysAlarmTypeDto sysAlarmType) {
		return sysAlarmMapper.typeList(Access.page(), sysAlarmType);
	}

	@Override
	public void typeAdd(SysAlarmTypeDto sysAlarmType) {
	    sysAlarmMapper.insertType(sysAlarmType);
	}

	@Override
	public void typeEdit(SysAlarmTypeDto sysAlarmType) {
	    sysAlarmMapper.updateType(sysAlarmType);
	}

	@Override
	public void typeDelete(Long id) {
	    sysAlarmMapper.deleteType(id);
	}

	@Override
	public Page<SysAlarmDto> list(SysAlarmDto sysAlarm) {
		return sysAlarmMapper.list(Access.page(), sysAlarm);
	}

	@Override
	public SysAlarmDto info(Long id) {
		return sysAlarmMapper.info(id);
	}

	@Override
	public void delete(Long id) {
	    sysAlarmMapper.delete(id);
	}

    @Override
    public void handle(AlarmHandles alarmHandles) {
        sysAlarmMapper.updateHandle(alarmHandles);
    }
}
