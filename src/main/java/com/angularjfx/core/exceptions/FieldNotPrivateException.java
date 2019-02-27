package com.angularjfx.core.exceptions;

public class FieldNotPrivateException extends RuntimeException {
    public FieldNotPrivateException(String name) {
        super(name + "must be private.");
    }
}
