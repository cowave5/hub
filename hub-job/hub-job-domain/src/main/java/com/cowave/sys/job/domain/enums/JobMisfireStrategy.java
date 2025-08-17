package com.cowave.sys.job.domain.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum JobMisfireStrategy implements EnumVal<Void> {

    /**
     * 忽略
     */
    IGNORE,

    /**
     * 立即执行
     */
    FIRE_NOW
}
