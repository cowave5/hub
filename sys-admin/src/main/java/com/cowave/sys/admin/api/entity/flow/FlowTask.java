/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.Date;

/**
 * 流程任务
 *
 * @author shanhuiming
 */
@Data
public class FlowTask {

	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务类型
	 */
	private String taskType;

	/**
	 * 执行id
	 */
	private String executionId;

	/**
	 * 业务标识
	 */
	private String businessKey;

	/**
	 * 流程id
	 */
	private String processInstanceId;

	/**
	 * 流程key
	 */
	private String processKey;

	/**
	 * 流程名称
	 */
	private String processName;

	/**
	 * 发起人
	 */
	private String starter;

	/**
	 * 发起人
	 */
	private String starterName;

	/**
	 * 委托人
	 */
	private String assignee;

	/**
	 * 委托人
	 */
	private String assigneeName;

	/**
	 * 流程开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	 * 任务开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date beginTime;

	/**
	 * 任务截止时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dueTime;

	/**
	 * 任务结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 表单标识
	 */
	private String formKey;

	/**
	 * 备注
	 */
	private String comment;

	public void fillTaskQuery(TaskQuery taskQuery){
        if (StringUtils.isNotEmpty(taskName)) {
			taskQuery.taskName(taskName);
        }
        if (StringUtils.isNotEmpty(processName)) {
			taskQuery.processDefinitionName(processName);
        }
		if (StringUtils.isNotEmpty(processKey)) {
			taskQuery.processDefinitionKey(processKey);
		}
		if(beginTime != null){
			taskQuery.taskCreatedAfter(beginTime);
		}
		if(endTime != null){
			taskQuery.taskCreatedBefore(endTime);
		}
	}

	public void fillHistoryTaskQuery(HistoricTaskInstanceQuery historyTaskQuery){
		if (StringUtils.isNotEmpty(taskName)) {
			historyTaskQuery.taskName(taskName);
		}
		if (StringUtils.isNotEmpty(processName)) {
			historyTaskQuery.processDefinitionName(processName);
		}
		if (StringUtils.isNotEmpty(processKey)) {
			historyTaskQuery.processDefinitionKey(processKey);
		}
		if(beginTime != null){
			historyTaskQuery.taskCompletedAfter(beginTime);
		}
		if(endTime != null){
			historyTaskQuery.taskCreatedBefore(endTime);
		}
	}
}
