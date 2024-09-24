/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.flow;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinitionQuery;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class FlowDeploy {

	/**
	 * 流程id
	 */
	private String id;

	/**
	 * 流程key
	 */
	private String key;

	/**
	 * 流程名称
	 */
	private String name;

	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 部署id
	 */
	private String deploymentId;

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 是否最新版本
	 */
	private boolean latest;

	private String diagramresourceName;

	public void fillProcessQuery(ProcessDefinitionQuery queryCondition){
		if (StringUtils.isNotEmpty(key)) {
			queryCondition.processDefinitionKey(key);
		}
		if (StringUtils.isNotEmpty(name)) {
			queryCondition.processDefinitionName(name);
		}
		if (latest) {
			queryCondition.latestVersion();
		}
	}
}
