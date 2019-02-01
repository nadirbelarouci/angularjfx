package com.angularjfx.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ComponentLoader {
    private Class<?> component;
    private List<NgModelBinding> bindings = new ArrayList<>();

    private ComponentLoader(Class<?> component) {
        this.component = component;

    }

    public static Parent load(Class<?> component) {
        ComponentLoader componentLoader = new ComponentLoader(component);
        return componentLoader.load();
    }

    private Parent load() {
        String templateUrl = component.getAnnotation(Component.class).templateUrl();
//        String selector = bootstrap.getAnnotation(Component.class).selector();
//        String[] styleUrls = bootstrap.getAnnotation(Component.class).styleUrls();
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(JavaFxAppLauncher.class.getClassLoader().getResource(templateUrl));
            Object controller = component.newInstance();
            loader.setController(controller);
            root = loader.load();

            bindings = NgModelBinding.bindProperties(controller, loader);
        } catch (InstantiationException
                | IllegalAccessException |
                IOException e) {
            throw new RuntimeException(e);
        }


        return root;
    }
}
