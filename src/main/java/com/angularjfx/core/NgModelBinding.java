package com.angularjfx.core;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NgModelBinding {
    private Field field;
    private Object instance;
    private StringProperty property = new SimpleStringProperty();
    ;
    private List<Control> nodes = new ArrayList<>();
    private boolean bindBidirectional;

    public NgModelBinding(Object instance, NgModel ngModel, FXMLLoader loader) {
        try {
            findField(instance, ngModel.bindFrom());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        bindBidirectional = ngModel.bindBidirectional();
        bind(ngModel.bindTo(), loader);
        System.out.println(nodes);
    }


    public static List<NgModelBinding> bindProperties(Object instance, FXMLLoader loader) {
        List<NgModelBinding> bindings = new ArrayList<>();
        for (Field field : instance.getClass().getDeclaredFields()) {
            NgModel[] ngModels = field.getAnnotationsByType(NgModel.class);
            for (NgModel ngModel : ngModels) {
                bindings.add(new NgModelBinding(instance, ngModel, loader));
            }
        }

        System.out.println(bindings.size());
        return bindings;
    }

    private void findField(Object instance, String bindFrom) throws NoSuchFieldException {
        if (!bindFrom.contains(".")) {
            this.instance = instance;
            this.field = instance.getClass().getDeclaredField(bindFrom);
            return;
        }

        String[] path = bindFrom.split("\\.");
        Field field = instance.getClass().getDeclaredField(path[0]);
        for (int i = 1; i < path.length; i++) {
            Pair<Object, Field> pair = getNext(instance, field, path[i]);
            instance = pair.getKey();
            field = pair.getValue();
        }

        this.instance = instance;
        this.field = field;
    }

    private Pair<Object, Field> getNext(Object instance, Field field, String name) {

        try {
            field.setAccessible(true);
            Object fieldInstance = field.get(instance);
            Field nextField = fieldInstance.getClass().getDeclaredField(name);
            return new Pair<>(fieldInstance, nextField);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    private void bind(String[] bindTo, FXMLLoader loader) {
        Stream.of(bindTo)
                .filter(id -> containsKey(loader, id))
                .map(id -> (Control) loader.getNamespace().get(id))
                .forEach(this::bind);
    }

    private boolean containsKey(FXMLLoader loader, String id) {
        if (loader.getNamespace().containsKey(id)) {
            return true;
        }
        throw new RuntimeException("No such field: " + id);
    }

    private void bind(Control control) {
        if (control instanceof TextInputControl) {
            TextInputControl node = (TextInputControl) control;
            bind(node.textProperty());
            nodes.add(node);
        } else if (control instanceof Labeled) {
            Labeled node = (Labeled) control;
            bind(node.textProperty());
            nodes.add(node);
        } else {
            throw new IllegalArgumentException("Can't bind to this node: " + control);
        }
    }

    private void bind(StringProperty stringProperty) {
        addListener();
        if (bindBidirectional) {
            Bindings.bindBidirectional(stringProperty, property);
        } else {
            stringProperty.bind(property);
        }
    }

    private void addListener() {
        try {


            property.set((String) getValue());
            property.addListener((observable, oldValue, newValue) -> setValue(newValue));

        } catch (ClassCastException e) {
            throw new RuntimeException(field.getName() + " is not a String", e);
        }
    }

    private Object getValue() {
        try {

            this.field.setAccessible(true);

            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValue(String newValue) {

        try {
            System.out.println(newValue);
            this.field.setAccessible(true);
            field.set(instance, newValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
