package com.cowave.hub.admin.domain.rabc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.commons.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum UserSex implements EnumVal<Integer> {

    /**
     * 男性
     */
    MALE(0),

    /**
     * 女性
     */
    FEMALE(1),

    /**
     * 未知
     */
    UNKNOWN(2);

    @EnumValue
    @JsonValue
    private final Integer val;
}
