package io.github.djoe.micrometer.springboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation use to declare a micrometer dynamic tag over method or class
 * {@code value} must be a valid SpEL expression
 *
 * @author djoedenne
 */
@Repeatable(DynamicTagSet.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicTag {
    String key();
    String value();
}
