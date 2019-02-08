package com.angularjfx.core;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * This transformer accepts classe declaring the "@CountInstance" annotation.
 * <p>
 * <p>
 * It enhances the class with an instanceCounter and outputs the value of ths
 * instanceCounter everytime an instance is built.
 */
public class NgModelTransformer extends AbstractTransformer implements ClassTransformer {


    /**
     * javassist has unfortunately no hasMethod API
     */
    private static boolean hasMethod(String methodName, CtClass cl) {
        try {
            cl.getDeclaredMethod(methodName);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }


    @Override
    public boolean accepts(CtClass cl) {
        return Stream.of(cl.getDeclaredFields()).
                anyMatch(f -> f.hasAnnotation(getAnnotationClass()));
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return NgModel.class;
    }

    @Override
    public void transform(CtClass cl) {
        try {
            if (!cl.isInterface()) {
                Stream.of(cl.getDeclaredFields())
                        .filter(f -> f.hasAnnotation(getAnnotationClass()))
                        .forEach(this::process);
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(CtField cf) {
        System.out.println(cf.getDeclaringClass().getSimpleName() + "." + cf.getName());
        try {
            NgModel ngModel = (NgModel) cf.getAnnotation(getAnnotationClass());
            String bindFrom = ngModel.bindFrom();

            System.out.println("ngModel.bindFrom() = " + ngModel.bindFrom());

            CtField targetField;
            if (bindFrom.equals(""))
                targetField = cf;
            else
                targetField = navigateToField(cf, bindFrom);

            System.out.println("targetField.getDeclaringClass() = " + targetField.getDeclaringClass().getName());

            CtClass targetClass = targetField.getDeclaringClass();

            process(targetClass, targetField);

        } catch (ClassNotFoundException
                | NotFoundException
                | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(CtClass targetClass, CtField targetField)
            throws NotFoundException, CannotCompileException,ClassNotFoundException {

        String name = targetField.getName();
        String nameProperty = name + "Property";

        String code = "private javafx.beans.property.StringProperty " + nameProperty +
                " = new javafx.beans.property.SimpleStringProperty(\"\");";

        CtField propertyField = CtField.make(code, targetClass);
        targetClass.addField(propertyField);
        addGetterSetter(targetClass, targetField);
        targetClass.instrument(new ExprEditor() {
            @Override
            public void edit(FieldAccess f) throws CannotCompileException {
                if (f.getFieldName().equals(name)) {
                    String camelCaseField = name.substring(0, 1).toUpperCase() + name.substring(1);
                    if (f.isWriter()) {
                        String setter = "set" + camelCaseField;
                        if (!f.where().getName().equals(setter)) {
                            f.replace(setter + "($1);");
                        } else {
                            f.replace(nameProperty + ".setValue($1);");
                        }
                    } else if (f.isReader()) {
                        String getter = "get" + camelCaseField;
                        if (!f.where().getName().equals(getter)) {
                            f.replace("$_ = " + getter + "();");
                        } else {
                            f.replace("$_ = " + nameProperty + ".getValue();");
                        }
                    }
                }
            }
        });

        propertyField.getFieldInfo()
                .addAttribute(targetField.getFieldInfo()
                        .getAttribute(AnnotationsAttribute.visibleTag));

        targetClass.removeField(targetField);
        CodeConverter codeConverter = new CodeConverter();
        codeConverter.redirectFieldAccess(propertyField, targetClass, name);
        targetClass.instrument(codeConverter);
        propertyField.setName(name);
    }

    private void addGetterSetter(CtClass cl, CtField field) throws CannotCompileException {
        String camelCaseField = field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
        if (!hasMethod("get" + camelCaseField, cl)) {
            cl.addMethod(CtNewMethod.getter("get" + camelCaseField, field));
        }

        if (!hasMethod("set" + camelCaseField, cl)) {
            cl.addMethod(CtNewMethod.setter("set" + camelCaseField, field));
        }
    }

    private CtField navigateToField(CtField start, String name) throws NotFoundException {
        if (name.contains(".")) {
            String[] names = name.split("\\.");
            for (int i = 1; i < names.length; i++) {
                start = start.getType().getField(names[i]);
            }
        }
        return start;
    }
}