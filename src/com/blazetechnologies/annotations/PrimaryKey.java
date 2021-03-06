package com.blazetechnologies.annotations;

import com.blazetechnologies.sql.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Dominic on 14/09/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
	boolean AutoIncrement() default false;
	Order Order() default Order.ASC;
}
