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
public enum EnableStatus implements EnumVal<Integer> {

    /**
     * 启用
     */
    ENABLE(1),

    /**
     * 停用
     */
    DISABLE(0);

    @EnumValue
    @JsonValue
    private final Integer val;
}
