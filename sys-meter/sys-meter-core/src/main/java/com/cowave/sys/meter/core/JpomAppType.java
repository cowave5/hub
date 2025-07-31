package com.cowave.sys.meter.core;

import com.cowave.sys.meter.core.common.Type;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JpomAppType {
    Type value();
}
