package com.angularjfx.core;

import javassist.CtClass;
import javassist.CtField;

import java.lang.annotation.Annotation;

/**
 * Base class for ClassTransformers.
 * <br />
 * Provides commodity routines for ClassTransformers and simplifies registration API.
 */
public abstract class AbstractTransformer implements ClassTransformer {

    @Override
    public  boolean accepts(CtClass cl) {
        return cl.hasAnnotation(getAnnotationClass());
    }

    /**
     * Classes that wants to be transformed by this transformer needs to declare
     * this annotation.
     *
     * @return the type of the annotation accepted by this transformer.
     */
    protected abstract Class<? extends Annotation> getAnnotationClass();

    @Override
    public abstract void transform(CtClass cl);
}