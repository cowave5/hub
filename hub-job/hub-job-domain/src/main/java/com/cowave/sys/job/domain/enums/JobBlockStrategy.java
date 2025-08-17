package com.cowave.sys.job.domain.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum JobBlockStrategy implements EnumVal<Void> {

    /**
     * 单机串行
     */
    SERIAL_EXECUTION,

    /**
     * 丢弃后续任务
     */
    DISCARD_LATER,

    /**
     * 覆盖之前任务
     */
    COVER_EARLY
}
