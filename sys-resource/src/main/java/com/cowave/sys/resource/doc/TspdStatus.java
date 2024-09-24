/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.doc;

import com.cowave.commons.framework.helper.dict.DictDoc;

/**
 * 转发器状态
 *
 * @author shanhuiming
 */
public enum TspdStatus implements DictDoc {

	/**
	 * 关闭
	 */
	CLOSE(0, "关闭"),

	/**
	 * 开启
	 */
	OPEN(1, "开启");

	private final int code;

	private final String desc;

	TspdStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}
