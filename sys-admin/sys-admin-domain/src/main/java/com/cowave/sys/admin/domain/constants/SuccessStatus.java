package com.cowave.sys.admin.domain.constants;

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
public enum SuccessStatus implements EnumVal<Integer> {

    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 失败
     */
    FAILED(0);

    @EnumValue
    @JsonValue
    private final Integer val;
}
