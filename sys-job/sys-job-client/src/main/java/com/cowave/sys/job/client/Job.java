package com.cowave.sys.job.client;

import java.lang.annotation.*;

/**
 * @author xuxueli/shanhuiming
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Job {

    String value();

    String init() default "";

    String destroy() default "";
}
