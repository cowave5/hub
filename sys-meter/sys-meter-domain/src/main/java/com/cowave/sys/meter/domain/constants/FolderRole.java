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
public enum FolderRole implements EnumVal<Integer> {

    /**
     * 参与人
     */
    PARTICIPANT(0),

    /**
     * 维护人
     */
    MAINTAINER(1),

    /**
     * 管理员
     */
    MANAGER(2);

    @EnumValue
    @JsonValue
    private final Integer val;
}
