package com.angularjfx.core;

import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StageConfiguration {
    private final String PREFIX = "application";
    private final Stage stage;
    private ApplicationProperties applicationProperties;

    public StageConfiguration(Stage stage) {
        this.stage = stage;
    }

    public void applyProperties() {
        if (getClass().getResource("/application.properties") == null)
            return;

        applicationProperties = new ApplicationProperties();

        Map<String, Class> params = new HashMap<>();
        params.put("AlwaysOnTop", boolean.class);
        params.put("FullScreen", boolean.class);
        params.put("FullScreenExitHint", boolean.class);
        params.put("MaxHeight", double.class);
        params.put("Maximized", boolean.class);
        params.put("MaxWidth", double.class);
        params.put("MinHeight", double.class);
        params.put("MinWidth", double.class);
        params.put("Resizable", boolean.class);
        params.put("Title", String.class);

        params.put("Height", double.class);
        params.put("Opacity", double.class);
        params.put("Width", double.class);
        params.put("X", double.class);
        params.put("Y", double.class);

        params.entrySet().stream().forEach(param ->
                applicationProperties
                        .computeIfPresent(PREFIX + "." + param.getKey().toLowerCase(), (k, v) ->
                                setAProperty(stage, param, v)));
    }

    private Object setAProperty(Stage stage, Map.Entry<String, Class> entry, Object propertyValue) {
        try {
            Method method = stage.getClass().getMethod("set" + entry.getKey(), entry.getValue());

            if (entry.getValue() == double.class) {
                method.invoke(stage, Double.valueOf(propertyValue.toString()));
            } else if (entry.getValue() == boolean.class) {
                method.invoke(stage, Boolean.valueOf(propertyValue.toString()));
            } else {
                method.invoke(stage, propertyValue);
            }
            return propertyValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
