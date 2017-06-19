package com.lenovo.common.entity;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


public class IgnoreStrategy implements ExclusionStrategy {

    /*
     * (non-Javadoc)
     *
     * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
     */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.
     * FieldAttributes)
     */
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        Collection<Annotation> annotations = fieldAttributes.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Ignore.class) {
                return true;
            }
        }
        return false;
    }

}

