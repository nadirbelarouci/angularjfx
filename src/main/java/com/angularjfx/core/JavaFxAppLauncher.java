package com.angularjfx.core;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxAppLauncher extends Application {
    private static Class<?> app;

    public static void run(Class<?> app, String... args) {
        JavaFxAppLauncher.app = app;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String title = app.getAnnotation(NgModule.class).title();
        int width = app.getAnnotation(NgModule.class).width();
        int height = app.getAnnotation(NgModule.class).height();

        Class<?>[] declarations = app.getAnnotation(NgModule.class).declarations();


        Class<?> bootstrap = app.getAnnotation(NgModule.class).bootstrap();

        Parent root = ComponentLoader.load(bootstrap);
        Scene scene = new Scene(root, width, height);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
