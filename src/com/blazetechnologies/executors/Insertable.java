package com.blazetechnologies.executors;

/**
 * Created by Dominic on 01/10/2015.
 */
public interface Insertable {
	/**
	 *
	 * @return the row id of the inserted row.
	 *
	 * */
	long executeInsert(String sql, Object[] args);
}
