package com.angularjfx.core;

import com.angularjfx.core.javaagent.JavaAgent;
import com.angularjfx.mock.A;
import com.angularjfx.mock.B;
import com.ea.agentloader.AgentLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class NgModelTransformerTest {

    @Before
    public void setUp() throws Exception {
        AgentLoader.loadAgentClass(JavaAgent.class.getName(), "");

    }


    @Test
    public void transform() throws Exception {


        B b = new B();
        int id = b.getC().getId();
        b.getC().increment();
        assertEquals(id + 1, b.getC().getId());

        A a = new A();

        assertEquals(LocalDate.now(), a.getDate());
//        assertEquals(SimpleStringProperty.class, A.class.getDeclaredField("title").getType());

        //        assertEquals(SimpleObjectProperty.class, A.class.getDeclaredField("date").getType());
//        assertEquals(int.class, A.class.getDeclaredField("whatever").getType());
//        assertEquals(SimpleIntegerProperty.class, C.class.getDeclaredField("id").getType());
//        assertEquals(SimpleStringProperty.class, C.class.getDeclaredField("name").getType());
//        assertEquals(SimpleBooleanProperty.class, C.class.getDeclaredField("isTrue").getType());

    }


}