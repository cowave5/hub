/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.operation.OperationAccepter;
import com.cowave.sys.admin.api.entity.LogQuery;
import com.cowave.sys.admin.api.service.SysLogService;
import com.cowave.sys.model.admin.SysLog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.api.mapper.SysLogMapper;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysLogServiceImpl implements SysLogService, OperationAccepter<SysLog> {

	private final ThreadPoolExecutor applicationExecutor;

	private final SysLogMapper sysLogMapper;

	@Override
	public void accept(SysLog sysLog) {
		applicationExecutor.execute(() -> sysLogMapper.insert(sysLog));
	}

	@Override
	public Page<SysLog> list(SysLog sysLog) {
		return sysLogMapper.list(Access.page(), sysLog);
	}

	@Override
	public SysLog info(Long id) {
		return sysLogMapper.info(id);
	}

	@Override
	public void delete(Long[] operId) {
		sysLogMapper.delete(operId);
	}

	@Override
	public void clean() {
		sysLogMapper.clean();
	}

	@Override
	public LogQuery userLogQuery(LogQuery logQuery) {
		if(CollectionUtils.isNotEmpty(logQuery.getRoleIds())){
			logQuery.setRoles(sysLogMapper.queryNameByRoleIds(logQuery.getRoleIds()));
		}
		if(CollectionUtils.isNotEmpty(logQuery.getParentIds())){
			logQuery.setParents(sysLogMapper.queryNameByUserIds(logQuery.getParentIds()));
		}
		if(CollectionUtils.isNotEmpty(logQuery.getDeptPostIds())){
			List<String> deptPost = new ArrayList<>();
			for(String deptPostId : logQuery.getDeptPostIds()){
				String[] arr = deptPostId.split("-");
				deptPost.add(sysLogMapper.queryNameOfDeptPost(Long.parseLong(arr[0]), Long.parseLong(arr[1])));
			}
			logQuery.setPosts(deptPost);
		}
		return logQuery;
	}

	@Override
	public LogQuery deptLogQuery(LogQuery logQuery) {
		if(CollectionUtils.isNotEmpty(logQuery.getParentIds())){
			logQuery.setParents(sysLogMapper.queryNameByDeptIds(logQuery.getParentIds()));
		}
		return logQuery;
	}

	@Override
	public LogQuery postLogQuery(LogQuery logQuery) {
		if(logQuery.getParentId() != null){
			logQuery.setParent(sysLogMapper.queryNameByPostId(logQuery.getParentId()));
		}
		return logQuery;
	}
}
