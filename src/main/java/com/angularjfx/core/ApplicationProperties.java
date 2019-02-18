package com.angularjfx.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties extends Properties {

    public ApplicationProperties() {
        super();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("application.properties");
            load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
