package com.angularjfx.core.javaagent;

import com.angularjfx.core.exceptions.FieldNotPrivateException;
import javafx.beans.property.*;
import javafx.util.Pair;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(PropertyTransformer.class);


    private static final Set<Pair<String, String>> tranformedSet = ConcurrentHashMap.newKeySet();
    private String propertyType;
    private CtClass clazz;
    private CtField field;
    private String propertyName;

    private PropertyTransformer(CtClass clazz, CtField field) {
        this.clazz = Objects.requireNonNull(clazz);
        this.field = Objects.requireNonNull(field);
        setPropertyType();
    }

    public static void transform(CtClass clazz, CtField field) {
        try {

            Pair<String, String> pair = new Pair<>(clazz.getName(), field.getName());
            if (!tranformedSet.contains(pair)) {
                new PropertyTransformer(clazz, field).transform();
                tranformedSet.add(pair);
            }
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPropertyType() {
        String typeName;
        try {
            typeName = field.getType().getName();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        switch (typeName) {
            case "int":
            case "java.lang.Integer":
                propertyType = SimpleIntegerProperty.class.getName();
                return;
            case "double":
            case "java.lang.Double":
                propertyType = SimpleDoubleProperty.class.getName();
                return;
            case "float":
            case "java.lang.Float":
                propertyType = SimpleFloatProperty.class.getName();
                return;
            case "boolean":
            case "java.lang.Boolean":
                propertyType = SimpleBooleanProperty.class.getName();
                return;
            case "java.lang.String":
                propertyType = SimpleStringProperty.class.getName();
                return;
            default:
//                propertyType = SimpleObjectProperty.class.getName() + "<" + typeName + ">";
        }
    }

    private ExprEditor getExprEditor() {
        return new FieldAccessExprEditor(field.getName(), propertyName);
    }

    private CtField make() {
        if (!Modifier.isPrivate(field.getModifiers()))
            throw new FieldNotPrivateException(field.getName());

        return new PropertyBuilder(clazz)
                .setType(propertyType)
                .setName(propertyName)
                .setAttributeInfo(field.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag))
                .build();
    }

    private void transform() throws CannotCompileException, NotFoundException {
        logger.info("Transforming {}.{}: {} to {}", clazz.getSimpleName(), field.getName(), field.getType().getSimpleName(),
                propertyType);


        this.propertyName = field.getName() + "Property";
        CtField propertyField = make();
        logger.info("Property make {} ", propertyField);

        clazz.instrument(getExprEditor());
        logger.info("Expr editor done for {} ", field.getName());
        clazz.removeField(field);

        CodeConverter codeConverter = new CodeConverter();
        codeConverter.redirectFieldAccess(propertyField, clazz, field.getName());
        clazz.instrument(codeConverter);
        propertyField.setName(field.getName());


        CtField result = clazz.getDeclaredField(field.getName());
        logger.info("Result {}.{}: {}", clazz.getSimpleName(), result.getName(),
                result.getType().getSimpleName());


    }

    private static class FieldAccessExprEditor extends ExprEditor {
        private String name;
        private String propertyName;

        private FieldAccessExprEditor(String name, String propertyName) {
            this.name = name;
            this.propertyName = propertyName;
        }

        @Override
        public void edit(FieldAccess f) throws CannotCompileException {
            if (f.getFieldName().equals(name)) {
                if (f.isWriter()) {
                    f.replace(propertyName + ".setValue($1);");
                } else if (f.isReader()) {
                    f.replace("$_ = " + propertyName + ".getValue();");
                }
            }
        }
    }

    private static class PropertyBuilder {
        private String type;
        private String name;
        private CtClass clazz;
        private AttributeInfo attributeInfo;

        private PropertyBuilder(CtClass clazz) {
            this.clazz = clazz;
        }

        private PropertyBuilder setType(String type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        private PropertyBuilder setName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }


        private PropertyBuilder setAttributeInfo(AttributeInfo attributeInfo) {
            this.attributeInfo = Objects.requireNonNull(attributeInfo);
            return this;
        }


        private CtField build() {
            try {
                String src = "private " + type + " " + name
                        + " = new " + type + "();";
                logger.info(src);
                CtField field = CtField.make(src, clazz);

                clazz.addField(field);
                if (attributeInfo != null)
                    field.getFieldInfo().addAttribute(attributeInfo);

                return field;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
