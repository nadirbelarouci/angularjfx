package com.angularjfx.core.annotations;

import com.angularjfx.core.annotations.NgModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})

public @interface NgModels {
    NgModel[] value();
}
