package com.angularjfx.core.javaagent;

import com.angularjfx.core.annotations.Component;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

public abstract class AbstractClassTransformer implements ClassTransformer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Class<? extends Annotation> fieldAnnotation;

    public AbstractClassTransformer(Class<? extends Annotation> fieldAnnotation) {
        this.fieldAnnotation = fieldAnnotation;
    }

    @Override
    public void transform(CtClass cl) {
        if (!cl.isInterface() && accept(cl)) {
            logger.info("Transforming {} ...", cl.getName());
            Stream.of(cl.getDeclaredFields())
                    .filter(field -> field.hasAnnotation(fieldAnnotation))
                    .forEach(this::transform);
            logger.info("{} transformation is done.", cl.getName());

        }
    }


    @Override
    public Class<? extends Annotation> annotatedWith() {
        return Component.class;
    }


    protected abstract void transform(CtField cf);

    protected CtField navigateToField(CtField start, String name) throws NotFoundException {
        if (name.contains(".")) {
            String[] names = name.split("\\.");
            for (int i = 1; i < names.length; i++) {
                start = start.getType().getField(names[i]);
            }
        }

        return start;
    }

    protected void transformToProperty(CtClass targetClass, CtField targetField) {
        PropertyTransformer.transform(targetClass, targetField);
    }

}
