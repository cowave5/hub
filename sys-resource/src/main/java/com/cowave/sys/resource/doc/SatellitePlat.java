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
 * 卫星平台
 *
 * @author shanhuiming
 */
public enum SatellitePlat implements DictDoc {

	/**
	 * DFH-3系列
	 */
	T0(0, "DFH-3系列"),

	/**
	 * DFH-4系列
	 */
	T1(1, "DFH-4系列"),

	/**
	 * DFH-5系列
	 */
	T2(2, "DFH-5系列"),

	/**
	 * DFH-6系列
	 */
	T3(3, "DFH-6系列");

	private final int code;

	private final String desc;

	SatellitePlat(int code, String desc) {
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
