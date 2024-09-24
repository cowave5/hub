/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.alarm.AlarmHandler;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysAlarmService;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysAlarmType;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.api.entity.AlarmHandles;
import com.cowave.sys.admin.api.mapper.SysAlarmMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysAlarmServiceImpl implements SysAlarmService, AlarmHandler<SysAlarm> {

	private final SysAlarmMapper sysAlarmMapper;


	@Override
	public void handle(SysAlarm sysAlarm) {
        if(sysAlarmMapper.alarmIncrease(sysAlarm) == 0) {
            sysAlarmMapper.insert(sysAlarm);
        }
	}

	@Override
	public Page<SysAlarmType> typeList(SysAlarmType sysAlarmType) {
		return sysAlarmMapper.typeList(Access.page(), sysAlarmType);
	}

	@Override
	public void typeAdd(SysAlarmType sysAlarmType) {
	    sysAlarmMapper.insertType(sysAlarmType);
	}

	@Override
	public void typeEdit(SysAlarmType sysAlarmType) {
	    sysAlarmMapper.updateType(sysAlarmType);
	}

	@Override
	public void typeDelete(Long id) {
	    Asserts.isNull(sysAlarmMapper.selectOne(id), "forbid.delete.alarm.type");
	    sysAlarmMapper.deleteType(id);
	}

	@Override
	public Page<SysAlarm> list(SysAlarm sysAlarm) {
		return sysAlarmMapper.list(Access.page(), sysAlarm);
	}

	@Override
	public SysAlarm info(Long id) {
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
