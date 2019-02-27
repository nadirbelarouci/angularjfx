package com.angularjfx.core.javaagent;

import com.angularjfx.core.annotations.NgModel;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NgModelTransformer extends AbstractClassTransformer {

    private static final Logger logger = LoggerFactory.getLogger(NgModelTransformer.class);

    public NgModelTransformer() {
        super(NgModel.class);
    }

    protected void transform(CtField cf) {
        try {
            logger.info("NgModel field: {}.{}", cf.getDeclaringClass().getSimpleName(), cf.getName());
            NgModel ngModel = (NgModel) cf.getAnnotation(NgModel.class);
            transform(cf, ngModel);
            logger.info("{}.{}: transformation finished.", cf.getDeclaringClass().getSimpleName(), cf.getName());

        } catch (ClassNotFoundException  e) {
            logger.error("Error transforming {}.{}: {} caused by: {}", cf.getDeclaringClass().getName(),
                    cf.getName(), e.getMessage(), e.getCause());
            throw new RuntimeException(e);
        }
    }

    protected void transform(CtField cf, NgModel ngModel) {
        String bindFrom = ngModel.bindFrom();


        CtField targetField;
        if (bindFrom.equals(""))
            targetField = cf;
        else try {
            targetField = navigateToField(cf, bindFrom);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        CtClass targetClass = targetField.getDeclaringClass();

        transformToProperty(targetClass, targetField);

    }

}