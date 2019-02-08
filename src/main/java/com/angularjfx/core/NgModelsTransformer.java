package com.angularjfx.core;

import javassist.CtClass;
import javassist.CtField;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * This transformer accepts classe declaring the "@CountInstance" annotation.
 * <p>
 * <p>
 * It enhances the class with an instanceCounter and outputs the value of ths
 * instanceCounter everytime an instance is built.
 */
public class NgModelsTransformer extends AbstractTransformer implements ClassTransformer {

    private NgModelTransformer ngModelTransformer = new NgModelTransformer();

    @Override
    public boolean accepts(CtClass cl) {
        return Stream.of(cl.getDeclaredFields()).
                anyMatch(f -> f.hasAnnotation(getAnnotationClass()));
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return NgModels.class;
    }


    @Override
    public void transform(CtClass cl) {
        try {
            if (!cl.isInterface()) {

                Stream<CtField> fields = Stream.of(cl.getDeclaredFields())
                        .filter(f -> f.hasAnnotation(getAnnotationClass()));

            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}