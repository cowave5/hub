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
 * 卫星类型
 *
 * @author shanhuiming
 */
public enum SatelliteType implements DictDoc {

	/**
	 * 军用
	 */
	T1(1, "军用"),

	/**
	 * 民用
	 */
	T2(2, "民用"),

	/**
	 * 军民两用
	 */
	T3(3, "军民两用");

	private final int code;

	private final String desc;

	SatelliteType(int code, String desc) {
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
