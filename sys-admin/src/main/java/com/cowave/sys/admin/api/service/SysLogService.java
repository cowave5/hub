/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.LogQuery;
import com.cowave.sys.model.admin.SysLog;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysLogService {

	void accept(SysLog sysLog);

	/**
	 * 列表
	 */
	Page<SysLog> list(SysLog sysLog);

	/**
	 * 详情
	 */
	SysLog info(Long id);

	/**
	 * 删除
	 */
	void delete(Long[] operId);

	/**
	 * 清空
	 */
	void clean();

	/**
	 * 用户日志信息查询
	 */
	LogQuery userLogQuery(LogQuery logQuery);

	/**
	 * 部门日志信息查询
	 */
	LogQuery deptLogQuery(LogQuery logQuery);

	/**
	 * 岗位日志信息查询
	 */
	LogQuery postLogQuery(LogQuery logQuery);
}
