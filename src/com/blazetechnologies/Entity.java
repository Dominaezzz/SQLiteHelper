package com.blazetechnologies;

import com.blazetechnologies.annotations.Table;

/**
 * Created by Dominic on 27/08/2015.
 */
public abstract class Entity {

    public Entity(){

    }

    public static <E extends Entity> String getEntityName(Class<E> table){
        String name = table.getSimpleName();
        if(table.isAnnotationPresent(Table.class)){
            name = table.getAnnotation(Table.class).Name();
        }
        return name;
    }

}
