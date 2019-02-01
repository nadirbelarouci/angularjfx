package com.angularjfx.core;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;

public class ChangeClassImplTest {
    @Test
    public void changeImpl() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.angularjfx.core.Example");
        CtMethod m = cc.getDeclaredMethod("doSomeThing");
        m.insertBefore("{ System.out.println(\"DoSomething\"); }");
        Class c = cc.toClass();
        Example e = (Example) c.newInstance();
        e.doSomeThing();
    }


}
