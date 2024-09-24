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
 * 通道类型
 *
 * @author shanhuiming
 */
public enum TunnelType implements DictDoc {

	/**
	 * 直通
	 */
	THROUGH(1, "直通"),

	/**
	 * 星上铰链
	 */
	HINGE(2, "星上铰链"),

	/**
	 * 多波束前向
	 */
	MULTI_FORWARD(3, "多波束前向"),

	/**
	 * 多波束返向
	 */
	MULTI_BACKWARD(4, "多波束返向");

	private final int code;

	private final String desc;

	TunnelType(int code, String desc) {
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
