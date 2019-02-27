package com.angularjfx.mock;

import com.angularjfx.core.annotations.Component;
import com.angularjfx.core.annotations.NgModel;

@Component
public class B {

    @NgModel(bindFrom = "c.id", bindTo = "NULL")
//    @NgModel(bindFrom = "c.name", bindTo = "NULL")
//    @NgModel(bindFrom = "c.a.title", bindTo = "NULL")
//    @NgModel(bindFrom = "c.isTrue", bindTo = "NULL")
    private C c = new C();

    public C getC() {
        return c;
    }
}
