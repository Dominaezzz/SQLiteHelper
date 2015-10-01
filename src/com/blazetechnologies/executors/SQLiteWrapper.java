package com.blazetechnologies.executors;

/**
 * Created by Dominic on 01/10/2015.
 */
public interface SQLiteWrapper<T> extends Executable, Insertable, Editable, Selectable<T> {
}
