package com.angularjfx.core;

public class Example {
    private int value;

    public Example() {
        this.value = 5;
    }

    public void doSomeThing() {
        value++;
        System.out.println("Done");
    }

    public void doSomethingElse() {
        value = 5;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}