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
        String name = table.getSimpleName();
        if(table.isAnnotationPresent(Table.class)){
            name = table.getAnnotation(Table.class).Name();
        }
        return name;
    }

    public static String getEntityFieldName(Field field){
		String name = field.getName();
		if(field.isAnnotationPresent(Column.class)){
			name = field.getDeclaredAnnotation(Column.class).Name();
		}
		return name;
	}

}
