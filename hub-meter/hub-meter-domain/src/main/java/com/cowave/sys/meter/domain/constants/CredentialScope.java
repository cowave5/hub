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
public enum CredentialScope implements EnumVal<Integer> {

    /**
     * 个人的
     */
    PERSONAL(0),

    /**
     * 指定的
     */
    SPECIFIED(1),

    /**
     * 全局的
     */
    EVERYONE(2);

    @EnumValue
    @JsonValue
    private final Integer val;
}
