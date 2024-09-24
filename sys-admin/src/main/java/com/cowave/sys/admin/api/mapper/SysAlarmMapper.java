/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.AlarmHandles;
import com.cowave.sys.model.admin.SysAlarm;
import com.cowave.sys.model.admin.SysAlarmType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysAlarmMapper {

	/**
	 * 类型列表
	 */
    Page<SysAlarmType> typeList(Page<SysAlarmType> page, @Param("type") SysAlarmType sysAlarmType);

	/**
	 * 类型新增
	 */
	void insertType(SysAlarmType sysAlarmType);

	/**
	 * 类型更新
	 */
	void updateType(SysAlarmType sysAlarmType);

    /**
     * 类型下存在告警
     */
    SysAlarm selectOne(Long id);

    /**
     * 类型删除
     */
    void deleteType(Long id);

    /**
     * 累计
     */
    int alarmIncrease(SysAlarm sysAlarm);

	/**
     * 新增
     */
    void insert(SysAlarm sysAlarm);

    /**
     * 列表
     */
    Page<SysAlarm> list(Page<SysAlarm> page, @Param("alarm") SysAlarm sysAlarm);

    /**
     * 详情
     */
    SysAlarm info(long id);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 处理
     */
    void updateHandle(AlarmHandles alarmHandles);
}
