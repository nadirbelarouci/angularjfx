package com.angularjfx.core;

import javassist.CtClass;

/**
 * A class Transformer transforms Java classes declaring the recognized
 * annotation(s) using Javassist.
 */
public interface ClassTransformer {

    /**
     * Used by the EnhancerAgent to know whether this class accepts the supported anotation
     *
     * @param cl the class to test
     * @return true if the passed annotation is accepted
     */
    boolean accepts(CtClass cl);


    /**
     * Proceed with the transformation of the javassist loaded class given as argument
     *
     * @param cl the javassist loaded class to be transformed
     */
    void transform(CtClass cl);

}