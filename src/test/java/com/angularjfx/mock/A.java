package com.angularjfx.mock;

import com.angularjfx.core.annotations.Component;
import com.angularjfx.core.annotations.NgModel;

import java.time.LocalDate;

@Component
public class A {
    @NgModel(bindTo = "NULL")
    private String title = "Hello world";

//    @NgModel(bindTo = "NULL")
    private LocalDate date = LocalDate.now();
    private int whatever = 0;

    public LocalDate getDate() {
        return date;
    }
}
