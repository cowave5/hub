package com.cowave.sys.job.infra.schedule;

import com.cowave.commons.tools.EnumVal;
import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum MisfireStrategyEnum implements EnumVal<Void> {

    /**
     * 忽略
     */
    IGNORE,

    /**
     * 立即执行
     */
    FIRE_NOW
}
