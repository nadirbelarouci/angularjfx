package com.angularjfx.core.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Repeatable(NgModels.class)
public @interface NgModel {
    String bindFrom() default "";

    String[] bindTo();

    boolean bindBidirectional() default true;
}
