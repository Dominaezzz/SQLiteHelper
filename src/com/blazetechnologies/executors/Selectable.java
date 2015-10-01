package com.blazetechnologies.executors;

/**
 * Created by Dominic on 01/10/2015.
 */
public interface Selectable<T> {
	/**
	 * This returns a result set of type T.
	 * Example:
	 *    On Android the result set is of type android.database.Cursor which is what this should return.
	 *
	 * @return This should return a result set of type T.
	 * */
	T executeQuery(String sql, Object[] args);
}
