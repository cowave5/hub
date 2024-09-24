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
 * 卫星轨道
 *
 * @author shanhuiming
 */
public enum SatelliteOrbit implements DictDoc {

    /**
     * 地球同步静止轨道
     */
    T0(0, "地球同步静止轨道"),

    /**
     * 中圆轨道
     */
    T1(1, "中圆轨道"),

    /**
     * 地球倾斜同步轨道
     */
    T2(2, "地球倾斜同步轨道"),

    /**
     * 太阳同步轨道
     */
    T3(3, "太阳同步轨道"),

    /**
     * 临界倾角轨道
     */
    T4(4, "临界倾角轨道");

    private final int code;

    private final String desc;

    SatelliteOrbit(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(int code) {
        SatelliteOrbit[] all = values();
        for (SatelliteOrbit satSatelliteOrbit : all) {
            if (code == satSatelliteOrbit.getCode()) {
                return satSatelliteOrbit.getDesc();
            }
        }
        return null;
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
