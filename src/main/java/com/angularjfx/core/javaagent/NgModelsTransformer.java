package com.angularjfx.core.javaagent;

import com.angularjfx.core.annotations.NgModels;
import javassist.CtField;

import java.util.stream.Stream;


public class NgModelsTransformer extends AbstractClassTransformer implements ClassTransformer {

    private static NgModelTransformer ngModelTransformer = new NgModelTransformer();

    public NgModelsTransformer() {
        super(NgModels.class);
    }


    protected void transform(CtField cf) {
        try {
            NgModels ngModels = (NgModels) cf.getAnnotation(NgModels.class);
            Stream.of(ngModels.value())
                    .forEach(ngModel -> ngModelTransformer.transform(cf, ngModel));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}