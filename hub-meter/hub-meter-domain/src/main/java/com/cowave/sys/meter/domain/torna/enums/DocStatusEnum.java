package com.cowave.sys.meter.domain.torna.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author thc
 */
@AllArgsConstructor
@Getter
public enum DocStatusEnum {
    TODO((byte) 0),
    DOING((byte) 5),
    DONE((byte)10);

    private final byte status;
}
