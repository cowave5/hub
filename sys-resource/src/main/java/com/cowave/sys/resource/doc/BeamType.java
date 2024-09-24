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
 * 波束类型
 *
 * @author shanhuiming
 */
public enum BeamType implements DictDoc {

	/**
	 * 国土波束
	 */
	T1(1, "国土波束"),

	/**
	 * 海域波束
	 */
	T2(2, "海域波束"),

	/**
	 * 固定点波束
	 */
	T3(3, "固定点波束"),

	/**
	 * 可移动点波束
	 */
	T4(4, "可移动点波束"),

	/**
	 * 固定区域波束
	 */
	T5(5, "固定区域波束"),

	/**
	 * 可移动区域波束
	 */
	T6(6, "可移动区域波束"),

	/**
	 * 馈电波束
	 */
	T7(7, "馈电波束"),

	/**
	 * 星间波束
	 */
	T8(8, "星间波束"),

	/**
	 * 其他波束
	 */
	T20(100, "其他波束");

	private final int code;

	private final String desc;

	BeamType(int code, String desc) {
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
