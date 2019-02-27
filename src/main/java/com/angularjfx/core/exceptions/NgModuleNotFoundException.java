package com.angularjfx.core.exceptions;

public class NgModuleNotFoundException extends RuntimeException {

    public NgModuleNotFoundException(String message) {
        super(message + " is not an AppModule.");
    }
}
