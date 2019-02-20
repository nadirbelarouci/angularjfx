package com.angularjfx.core;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JavaFxAppLauncher extends Application {
    private static Class<?> app;

    public static void run(Class<?> app, String... args) {
        JavaFxAppLauncher.app = app;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Class<?>[] declarations = app.getAnnotation(NgModule.class).declarations();

        Class<?> bootstrap = app.getAnnotation(NgModule.class).bootstrap();

        Parent root = ComponentLoader.load(bootstrap);
        Scene scene = new Scene(root);

        StageConfiguration stageConfiguration = new StageConfiguration(stage);
        stageConfiguration.applyProperties();

        stage.setScene(scene);
        stage.show();
    }



}
