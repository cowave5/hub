package com.cowave.sys.admin.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum YesNo {

    /**
     * 是
     */
    YES(1),

    /**
     * 否
     */
    NO(0);

    @EnumValue
    @JsonValue
    private final Integer val;
}
