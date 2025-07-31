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
public enum NoticeLevel implements EnumVal<Integer> {

    /**
     * 普通
     */
    COMMON(0),

    /**
     * 紧急
     */
    URGENT(1);

    @EnumValue
    @JsonValue
    private final Integer val;
}
