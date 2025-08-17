package com.cowave.sys.meter.domain.torna;

import com.cowave.sys.meter.domain.torna.enums.PositionType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Diff {

    PositionType positionType();

}
