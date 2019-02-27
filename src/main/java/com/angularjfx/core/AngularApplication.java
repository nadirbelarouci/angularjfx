package com.angularjfx.core;

import com.angularjfx.core.annotations.Component;
import com.angularjfx.core.annotations.NgModule;
import com.angularjfx.core.exceptions.ComponentNotFoundException;
import com.angularjfx.core.exceptions.NgModuleNotFoundException;
import com.angularjfx.core.javaagent.JavaAgent;
import com.ea.agentloader.AgentLoader;

public class AngularApplication {
    public static void run(Class<?> app, String... args) {

        AgentLoader.loadAgentClass(JavaAgent.class.getName(), "");

        if (app == null) {
            throw new IllegalArgumentException("Null reference to the AppModule");
        }
        if (!app.isAnnotationPresent(NgModule.class)) {
            throw new NgModuleNotFoundException(app.getSimpleName());
        }

        Class<?>[] declarations = app.getAnnotation(NgModule.class).declarations();
        for (Class<?> clazz : declarations) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                throw new ComponentNotFoundException(clazz.getSimpleName());
            }
        }

        Class<?> bootstrap = app.getAnnotation(NgModule.class).bootstrap();
        if (!bootstrap.isAnnotationPresent(Component.class)) {
            throw new ComponentNotFoundException(bootstrap.getSimpleName());
        }
        JavaFxAppLauncher.run(app, args);
    }
}