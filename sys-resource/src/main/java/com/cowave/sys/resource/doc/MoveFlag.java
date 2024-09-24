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
 * 波束移动标识
 *
 * @author shanhuiming
 */
public enum MoveFlag implements DictDoc {

	/**
	 * 固定
	 */
	STATIC(0, "固定"),

	/**
	 * 移动
	 */
	MOVE(1, "移动");

	private final int code;

	private final String desc;

	MoveFlag(int code, String desc) {
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
