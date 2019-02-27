package com.angularjfx.mock;

import com.angularjfx.core.annotations.Component;

@Component
public class C {
    private int id;
    private String name;
    private boolean isTrue;
    private A a = new A();

    public void increment() {
        id++;
    }

    public int getId() {
        return id;
    }
}