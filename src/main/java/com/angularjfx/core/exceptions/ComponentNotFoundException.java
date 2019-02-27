package com.angularjfx.core.exceptions;

public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(String message) {
        super(message + " is not a component.");
    }
}
