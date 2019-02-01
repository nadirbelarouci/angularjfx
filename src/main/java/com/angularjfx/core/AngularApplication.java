package com.angularjfx.core;

public class AngularApplication {
    public static void run(Class<?> app, String... args) {
        if (app == null) {
            throw new IllegalArgumentException("Null reference to the AppModule");
        }
        if (!app.isAnnotationPresent(NgModule.class)) {
            throw new NotAnAppModuleException(app.getSimpleName() + " is not an AppModule");
        }

        Class<?>[] declarations = app.getAnnotation(NgModule.class).declarations();
        for (Class<?> clazz : declarations) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                throw new NotAComponentException(clazz.getSimpleName() + " is not a component");
            }
        }

        Class<?> bootstrap = app.getAnnotation(NgModule.class).bootstrap();
        if (!bootstrap.isAnnotationPresent(Component.class)) {
            throw new NotAComponentException(bootstrap.getSimpleName() + " is not a component");
        }


        JavaFxAppLauncher.run(app, args);
    }
}
