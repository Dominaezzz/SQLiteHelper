package com.blazetechnologies.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dominic on 27/08/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String Name();
    boolean PrimaryKey() default false;
    boolean AutoIncrement() default false;
    boolean Unique() default false;
    boolean Null() default true;
}
