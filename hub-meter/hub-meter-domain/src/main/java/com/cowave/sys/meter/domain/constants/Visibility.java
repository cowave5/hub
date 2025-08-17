package com.cowave.sys.meter.domain.constants;

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
public enum Visibility implements EnumVal<Integer> {

    /**
     * 公开的
     */
    PUBLIC(0),

    /**
     * 私有的
     */
    PRIVATE(1);

    @EnumValue
    @JsonValue
    private final Integer val;
}
