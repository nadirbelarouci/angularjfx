package com.angularjfx.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NgModule {
    Class<?>[] declarations() default {};

    Class<?> bootstrap();

    int width() default 300;

    int height() default 250;

    String title() default "AngularFx Application";

}
