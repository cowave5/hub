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
 * 极化方式
 *
 * @author shanhuiming
 */
public enum Polarization implements DictDoc {

	/**
	 * 水平极化
	 */
	T1(1, "水平极化"),

	/**
	 * 垂直极化
	 */
	T2(2, "垂直极化"),

	/**
	 * 左旋圆极化
	 */
	T3(3, "左旋圆极化"),

	/**
	 * 右旋圆极化
	 */
	T4(4, "右旋圆极化");

	private final int code;

	private final String desc;

	Polarization(int code, String desc) {
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
