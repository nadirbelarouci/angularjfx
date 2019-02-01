package com.angularjfx.core;

import javafx.util.Pair;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertSame;

public class NgModelBindingTest {
    @Test
    public void testFindField() throws Exception {
        A a = new A();
        Pair<Object, Field> pair = findField(a, "str");
        assertSame(a, pair.getKey());
        pair.getValue().setAccessible(true);
        assertSame(a.str, pair.getValue().get(a));

        B b = new B();
        pair = findField(b, "c.a");
        assertSame(b.c, pair.getKey());
        pair.getValue().setAccessible(true);
        assertSame(b.c.a, pair.getValue().get(b.c));

        pair = findField(b, "c.a.str");
        assertSame(b.c.a, pair.getKey());
        pair.getValue().setAccessible(true);
        assertSame(b.c.a.str, pair.getValue().get(b.c.a));
    }

    private Pair<Object, Field> findField(Object instance, String bindFrom) throws NoSuchFieldException {
        if (!bindFrom.contains(".")) {
            Field field = instance.getClass().getDeclaredField(bindFrom);
            field.setAccessible(true);
            return new Pair<>(instance, field);
        }

        String[] path = bindFrom.split("\\.");

        Field field = instance.getClass().getDeclaredField(path[0]);
        for (int i = 1; i < path.length; i++) {
            Pair<Object, Field> pair = getNext(instance,field,path[i]);
            instance = pair.getKey();
            field = pair.getValue();
        }
        return new Pair<>(instance,field);
    }

    private Pair<Object, Field> getNext(Object instance, Field field, String name) {

        try {
            field.setAccessible(true);
            Object fieldInstance = field.get(instance);
            Field nextField = fieldInstance.getClass().getDeclaredField(name);
            return new Pair<>(fieldInstance, nextField);

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    private class A {
        private String str = "Hello world";
    }

    private class B {
        private C c = new C();
    }

    private class C {
        private A a = new A();
    }


}