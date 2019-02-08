package com.angularjfx.core;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * This is a JVM Agent that implements classes transformation from recognized
 * annotations.
 * <p/>
 * <p>
 * The EnhancerAgent is called before the application starts and registers a
 * <code>java.lang.instrument.ClassFileTransformer</code> that enhances
 * classes declaring specific annotations before they are loaded
 * by classloader(s).
 * <p/>
 * <p>
 * The <code>java.lang.instrument.ClassFileTransformer</code> here is a simple
 * anonymous adapter.
 */
public class EnhancerAgent {

    private static ClassTransformer[] transformers = null;

    // for now I don't have any better way than declaring all transformers here
    static {
        transformers = new ClassTransformer[]{
                new NgModelTransformer()
        };
    }

    // Java Agent API
    public static void premain(String agentArgs, Instrumentation inst) {
        agentmain(agentArgs, inst);
    }

    // API used when agent invoked after JVM Startup
    public static void agentmain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(EnhancerAgent::transform);
    }

    public static byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {

        // Can return null if no transformation is performed
        byte[] transformedClass = null;

        CtClass cl = null;
        ClassPool pool = ClassPool.getDefault();
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));

            for (ClassTransformer transformer : transformers) {

                if (transformer.accepts(cl)) {
                    transformer.transform(cl);
                    System.out.println("Transformed class " + cl.getName()
                            + " with " + transformer.getClass().getSimpleName());
                }
            }

            // Generate changed bytecode
            transformedClass = cl.toBytecode();

        } catch (IOException | CannotCompileException e) {
            throw new RuntimeException(e);

        } finally {
            if (cl != null) {
                cl.detach();
            }
        }

        return transformedClass;
    }
}