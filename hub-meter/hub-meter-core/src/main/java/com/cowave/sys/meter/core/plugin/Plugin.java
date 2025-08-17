package com.cowave.sys.meter.core.plugin;

import java.lang.annotation.*;

/**
 * @author bwcx_jzy
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {

    String name();

    boolean nativeObject() default true;
}
