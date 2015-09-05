package com.blazetechnologies;

import com.blazetechnologies.annotations.Table;

/**
 * Created by Dominic on 27/08/2015.
 */
public class Utils {

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static <E extends Entity> String getTableName(Class<E> table){
        String name = table.getSimpleName();
        if(table.isAnnotationPresent(Table.class)){
            name = table.getAnnotation(Table.class).Name();
        }
        return name;
    }

}
