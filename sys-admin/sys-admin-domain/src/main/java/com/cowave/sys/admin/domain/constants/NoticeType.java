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
public enum NoticeType implements EnumVal<Integer> {

    /**
     * 公告
     */
    ANNOUNCEMENT(0),

    /**
     * 通知
     */
    NOTICE(1),

    /**
     * 催办
     */
    PRESS(3);

    @EnumValue
    @JsonValue
    private final Integer val;
}
