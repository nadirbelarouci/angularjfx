package com.angularjfx.core.javaagent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.stream.Stream;


public class JavaAgent {
    private static final Logger logger = LoggerFactory.getLogger(JavaAgent.class);
    private static ClassTransformer[] transformers;

    static {

        String angularjfx = "\n\n" +
                "    ___                      __               _________  __\n" +
                "   /   |  ____  ____ ___  __/ /___ ______    / / ____/ |/ /\n" +
                "  / /| | / __ \\/ __ `/ / / / / __ `/ ___/_  / / /_   |   / \n" +
                " / ___ |/ / / / /_/ / /_/ / / /_/ / /  / /_/ / __/  /   |  \n" +
                "/_/  |_/_/ /_/\\__, /\\__,_/_/\\__,_/_/   \\____/_/    /_/|_|  \n" +
                "             /____/                                        \n";

        logger.info(angularjfx);

        logger.info("Initializing transformers ...");
        transformers = new ClassTransformer[]{
                new NgModelTransformer(),
                new NgModelsTransformer()
        };
        logger.info("Initializing transformers finished");

    }


    public static void premain(String args, Instrumentation inst) {
        agentmain(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {

        logger.info("Instrumentation is starting ...");
        inst.addTransformer(JavaAgent::transform);
    }

    public static byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {

        // Can return null if no transformation is performed
        byte[] transformedClass = null;


        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            Stream.of(transformers)
                    .filter(t -> t.accept(cl))
                    .forEach(t -> t.transform(cl));
            // Generate changed bytecode
            transformedClass = cl.toBytecode();

        } catch (IOException | CannotCompileException e) {
            logger.error("CannotCompileException: {} caused by: {}", e.getMessage(), e.getCause());
            throw new RuntimeException(e);

        } finally {

        }

        return transformedClass;
    }
}