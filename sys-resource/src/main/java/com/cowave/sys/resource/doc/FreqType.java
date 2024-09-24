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
 * 频段类型
 *
 * @author shanhuiming
 */
public enum FreqType implements DictDoc {

	/**
	 * UHF
	 */
	UHF(1, "UHF"),

	/**
	 * C
	 */
	C(2, "C"),

	/**
	 * Ku
	 */
	KU(3, "Ku"),

	/**
	 * Ka
	 */
	KA(4, "Ka"),

	/**
	 * S
	 */
	S(5, "S"),

	/**
	 * X
	 */
	X(6, "X"),

	/**
	 *
	 */
	UNKNOWN(20, "unknown");

	private final int code;

	private final String desc;

	FreqType(int code, String desc) {
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
