package com.cowave.sys.job.domain.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum JobTriggerType implements EnumVal<Void> {

    /**
     * 手动触发
     */
    MANUAL,

    /**
     * 定时触发
     */
    SCHEDULE,

    /**
     * Api触发
     */
    API,

    /**
     * 过期补偿
     */
    MISFIRE
}
