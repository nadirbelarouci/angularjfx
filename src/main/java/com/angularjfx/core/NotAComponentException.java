package com.angularjfx.core;

public class NotAComponentException extends RuntimeException {
    public NotAComponentException() {
        super();
    }

    public NotAComponentException(String message) {
        super(message);
    }
}
