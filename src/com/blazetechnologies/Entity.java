package com.blazetechnologies;

import com.blazetechnologies.annotations.Column;
import com.blazetechnologies.annotations.Table;

import java.lang.reflect.Field;

/**
 * Created by Dominic on 27/08/2015.
 */
public abstract class Entity {

    protected Entity(){

    }

    public static <E extends Entity> String getEntityName(Class<E> table){
		return table.isAnnotationPresent(Table.class) ? table.getAnnotation(Table.class).Name() : table.getSimpleName();
    }

    public static String getEntityFieldName(Field field){
		return field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).Name() : field.getName();
	}

}
