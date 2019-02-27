package com.angularjfx.core.javaagent;

import javassist.CtClass;

import java.lang.annotation.Annotation;

public interface ClassTransformer {

    void transform(CtClass cl);

    Class<? extends Annotation> annotatedWith();

    default boolean accept(CtClass cl) {
        return cl.hasAnnotation(annotatedWith());
    }
}